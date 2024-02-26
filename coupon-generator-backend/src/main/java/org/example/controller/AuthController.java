package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.Responses.MessageResponse;
import org.example.entity.RefreshTokenEntity;
import org.example.exception.TokenRefreshException;
import org.example.service.RefreshTokenService;
import org.example.service.TokenService;
import org.example.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenService tokenService;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    private JWTUtils jwtUtils;

    public AuthController(TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<MessageResponse> refreshToken(HttpServletRequest request){
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
        String jwt =jwtUtils.getJwtFromCookies(request);

        boolean validateBrowser = jwtUtils.validateBrowserDetails(jwt, request.getRemoteAddr(), request.getHeader("user-agent"));

        if((refreshToken != null) && (refreshToken.isEmpty())){
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshTokenEntity::getUserEntity)
                    .map(userEntity -> {
                        if(validateBrowser){
                            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userEntity, request.getRemoteAddr(), request.getHeader("user-agent"));
                            String jwtToken = (jwtCookie.toString().split("=")[1]).split(";")[0];

                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                                    .body(new MessageResponse("Token is refreshed successfully!"));
                        }
                        return ResponseEntity.badRequest()
                                .body(new MessageResponse("Invalid Source"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is Empty!"));
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
