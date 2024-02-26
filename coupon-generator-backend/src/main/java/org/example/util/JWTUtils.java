package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Date;

@Component
public class JWTUtils {

    @Value("${couponGenerator.jwtCookieName}")
    private String jwtCookie;

    @Value("${couponGenerator.jwtSecret}")
    private String jwtSecret;

    @Value("${couponGenerator.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${couponGenerator.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookie);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    private String getCookieValueByName(HttpServletRequest request, String name){
        Cookie cookie = WebUtils.getCookie(request, name);
        if(cookie != null){
            return cookie.getValue();
        }else {
            return null;
        }
    }

    public boolean validateBrowserDetails(String jwt, String remoteAddr, String header) {
        return true;
    }

    public ResponseCookie generateJwtCookie(UserEntity userEntity, String ipAddress, String userAgent) {
        String jwt = generateTokenFromUsername(userEntity.getUserName(), ipAddress, userAgent);
        return generateCookie(jwtCookie, jwt, "/auth");
    }

    public String generateTokenFromUsername(String username, String ipAddress, String userAgent) {
        Claims claims = Jwts.claims();
        claims.put("sub", username);
        claims.put("ip", ipAddress);
        claims.put("userAgent", userAgent);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value).path(path).maxAge(24 * 60 * 60).secure(true).httpOnly(true).build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null).path("/auth").build();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookie, null).path("/api/auth").build();
    }

}
