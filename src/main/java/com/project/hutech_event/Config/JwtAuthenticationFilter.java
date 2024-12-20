package com.project.hutech_event.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtTokenProvider; // Lớp xử lý JWT

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy Authorization header từ request
        String authHeader = request.getHeader("Authorization");

        // Kiểm tra xem header có hợp lệ không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Lấy token

            // Kiểm tra token hợp lệ
            if (jwtTokenProvider.validateJwtToken(token)) {
                // Lấy username từ token
                String username = jwtTokenProvider.getUsernameFromJwtToken(token);

                // Lấy danh sách vai trò từ token
                List<String> roles = jwtTokenProvider.getRolesFromToken(token);

                // Chuyển roles thành danh sách GrantedAuthority
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Tạo đối tượng Authentication và đặt vào SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Tiếp tục chuỗi filter
        filterChain.doFilter(request, response);
    }
}