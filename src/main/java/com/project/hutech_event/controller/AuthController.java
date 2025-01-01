package com.project.hutech_event.controller;

import com.project.hutech_event.blacklist.TokenBlackList;
import com.project.hutech_event.dto.request.IntrospectRequest;
import com.project.hutech_event.dto.request.LoginRequest;
import com.project.hutech_event.dto.request.RegisterRequest;
import com.project.hutech_event.dto.response.IntrospectResponse;
import com.project.hutech_event.dto.response.LoginResponse;
import com.project.hutech_event.dto.response.RegisterResponse;
import com.project.hutech_event.service.AuthenticationService;
import com.project.hutech_event.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlackList tokenBlacklist;

    /**
     * Đăng nhập
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse response = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Đăng xuất
     */
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Lấy token từ header "Bearer token"
        tokenBlacklist.addToken(token);
        return ResponseEntity.ok("Token has been invalidated successfully");
    }

    /**
     * Kiểm tra token có hợp lệ không
     */
    @GetMapping("/introspect")
    public ResponseEntity<IntrospectResponse> authenticate(@RequestHeader("Authorization") String authHeader) {
        IntrospectResponse response = authenticationService.isTokenValid(authHeader);
        return ResponseEntity.ok(response);
    }

    /**
     * Đăng ký
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }
}
