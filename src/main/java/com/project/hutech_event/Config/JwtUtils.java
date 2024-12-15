package com.project.hutech_event.Config;

import com.project.hutech_event.blacklist.TokenBlackList;
import com.project.hutech_event.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private final String jwtSecret = "YxYEXg9Dm5a1oYHW1vKyg9xGL6ozJUyxbX0rq0yAKRvv7s/w+2QPPcRWw+kGMEgu";
    private final int jwtExpirationMs = 43200000;

    @Autowired
    private TokenBlackList tokenBlackList;

    public String generateJwtToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {

            if (tokenBlackList.isTokenBlacklisted(authToken)) {
                System.out.println("Token is blacklisted");
                return false;
            }

            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            System.out.println("JWT không tồn tại: " + e.getMessage());
        }
        return false;
    }
}
