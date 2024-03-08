package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.AdminDTO;
import org.example.dto.Request.RefreshTokenRequest;
import org.example.dto.Request.SignInRequest;
import org.example.dto.Request.SignUpRequest;
import org.example.dto.Responses.ApiResponse;
import org.example.dto.Responses.JwtResponse;
import org.example.dto.Responses.MessageResponse;
import org.example.dto.UserDTO;
import org.example.entity.*;
import org.example.repository.AdminRepository;
import org.example.repository.UserRepository;
import org.example.service.AdminService;
import org.example.service.RefreshTokenService;
import org.example.service.UserService;
import org.example.service.impl.JwtService;
import org.example.util.JWTUtils;
import org.example.util.Messages;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @Autowired
    Messages messages;

    @PostMapping("/user/sign-in")
    public JwtResponse userSignIn(@RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUserName(), signInRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(signInRequest.getUserName());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(signInRequest.getUserName(),signInRequest.getEmail()))
                    .token(refreshToken.getToken()).build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

//    @PostMapping("/admin/sign-in")
//    public JwtResponse adminSignIn(@RequestBody SignInRequest signInRequest, HttpServletRequest request) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUserName(), signInRequest.getPassword()));
//        if (authentication.isAuthenticated()) {
//            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(signInRequest.getUserName());
//            return JwtResponse.builder()
//                    .accessToken(jwtService.generateToken(signInRequest.getUserName(),signInRequest.getEmail(),signInRequest.getRole()))
//                    .token(refreshToken.getToken()).build();
//        } else {
//            throw new UsernameNotFoundException("invalid user request !");
//        }
//    }

    @PostMapping("/refresh-token")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUserEntity)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUserName(),userInfo.getEmail());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }

    @PostMapping("/user/sign-up")
    public ApiResponse registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_NAME_TAKEN, null));
            return apiResponse;
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.EMAIL_IN_USE, null));
            return apiResponse;
        }
        UserDTO userDTO = new UserDTO(signUpRequest.getUserName(), signUpRequest.getEmail(), signUpRequest.getPassword());

        return userService.signUpUser(userDTO, signUpRequest.getAdminId());
    }

    @PostMapping("/admin/sign-up")
    public ApiResponse registerAdmin(@RequestBody SignUpRequest signUpRequest){
        if(adminService.isExistByName(signUpRequest.getUserName())){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(RequestStatus.BAD_REQUEST.getStatusCode());
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_NAME_TAKEN, null));
            return apiResponse;
        }
        if(adminService.isExistByEmail(signUpRequest.getEmail())){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(RequestStatus.BAD_REQUEST.getStatusCode());
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.EMAIL_IN_USE, null));
            return apiResponse;
        }
        AdminDTO adminDTO = new AdminDTO(signUpRequest.getUserName(), signUpRequest.getPassword(), signUpRequest.getEmail());

        return adminService.signUpAdmin(adminDTO);
    }

    @GetMapping("/sign-out")
    public ResponseEntity<MessageResponse> logOutUser(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
        refreshTokenService.deleteRefreshToken(refreshToken);

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse(messages.getMessageForResponseCode(ResponseCodes.USER_SIGNED_OUT, null)));
    }
}
