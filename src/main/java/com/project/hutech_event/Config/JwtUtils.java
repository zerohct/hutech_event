package com.project.hutech_event.Config;

import com.project.hutech_event.blacklist.TokenBlackList;
import com.project.hutech_event.model.Role;
import com.project.hutech_event.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final String jwtSecret = "YxYEXg9Dm5a1oYHW1vKyg9xGL6ozJUyxbX0rq0yAKRvv7s/w+2QPPcRWw+kGMEgu";
    private final int jwtExpirationMs = 43200000;

    @Autowired
    private TokenBlackList tokenBlackList;

    public String generateJwtToken(User user) {
        // Thêm thông tin roles và userId vào claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream()
                .map(Role::getName) // Chỉ lưu tên vai trò
                .collect(Collectors.toList()));
        claims.put("userId", user.getUserId()); // Thêm userId vào claims

        return Jwts.builder()
                .setClaims(claims) // Đặt claims
                .setSubject(user.getUsername()) // Đặt username làm subject
                .setIssuedAt(new Date()) // Thời điểm phát hành token
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Thời điểm hết hạn token
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes()) // Ký token
                .compact(); // Tạo token
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

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return (List<String>) claims.get("roles");
    }

    public Long getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class); // Lấy userId từ claims
    }

}
