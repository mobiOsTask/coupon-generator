package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.Request.ApiRequest;
import org.example.dto.Request.RefreshTokenRequest;
import org.example.dto.Request.SignUpRequest;
import org.example.dto.Responses.JwtResponse;
import org.example.dto.Responses.MessageResponse;
import org.example.entity.*;
import org.example.repository.AdminRepository;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserRoleRepository;
import org.example.service.RefreshTokenService;
import org.example.service.impl.JwtService;
import org.example.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/sign-in")
    public JwtResponse signIn(@RequestBody ApiRequest authRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(authRequest.getUserName());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUserName()))
                    .token(refreshToken.getToken()).build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/refresh-token")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUserEntity)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUserName());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody SignUpRequest signUpRequest){
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByAddress(signUpRequest.getAddress())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        if(!adminRepository.existsById(signUpRequest.getAdminId())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Admin not found!"));
        }

        Optional<AdminEntity> adminEntity = adminRepository.findById(signUpRequest.getAdminId());

        RolesEntity rolesEntity = roleRepository.findByName(ERole.valueOf(signUpRequest.getRoleName()));

        if (adminEntity.isPresent()) {
            UserEntity userEntity = new UserEntity(
                    signUpRequest.getUserName(),
                    signUpRequest.getAddress(),
                    bCryptPasswordEncoder.encode(signUpRequest.getPassword())
            );

            userEntity.setCreatedAdmin(adminEntity.get());

            UserEntity savedUserEntity = userRepository.save(userEntity);

            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRoleEntity(rolesEntity);
            userRoleEntity.setName("name");
            userRoleEntity.setPosition("position");
            userRoleEntity.setStatus("active");
            userRoleEntity.setDescription("description");
            userRoleEntity.setUserEntity(savedUserEntity);

            userRoleRepository.save(userRoleEntity);

            Optional<UserRoleEntity> userRole = userRoleRepository.findById(1);

            if (userRole.isPresent()) {
                savedUserEntity.setUserRoleEntity(userRole.get());
                userRepository.save(savedUserEntity);

                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("UserRoleEntity with ID 1 not found"));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Admin not found"));
        }
    }

    @GetMapping("/signout")
    public ResponseEntity<MessageResponse> logOutUser(HttpServletRequest request){
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
        refreshTokenService.deleteRefreshToken(refreshToken);

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

}
