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

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
   private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlackList tokenBlacklist;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
        return authenticationService.authenticate(loginRequest);
    }

    @GetMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Lấy token từ header "Bearer token"
        tokenBlacklist.addToken(token);
        return "Token has been invalidated successfully";
    }

    //kiểm tra token có  hợp lệ không
    @GetMapping("/introspect")
    public IntrospectResponse authenticate(@RequestHeader("Authorization") String authHeader) {
        return authenticationService.isTokenValid(authHeader);
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }


}
