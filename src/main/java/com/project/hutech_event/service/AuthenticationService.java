package com.project.hutech_event.service;


import com.project.hutech_event.Config.JwtUtils;
import com.project.hutech_event.dto.request.LoginRequest;
import com.project.hutech_event.dto.response.IntrospectResponse;
import com.project.hutech_event.dto.response.LoginResponse;
import com.project.hutech_event.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public LoginResponse authenticate(LoginRequest loginRequest) throws Exception {
        // Xác thực username và password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        var user = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new Exception("Người dùng không tồn tại"));

        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!authenticated) throw new Exception("Mật khẩu sai");;

        var token = jwtUtils.generateJwtToken(user);

        // Trả về LoginResponse
        return LoginResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    // Kiểm tra tính hợp lệ của token
    public IntrospectResponse isTokenValid(String request) {
        String token = request.substring(7);
        boolean  valid =  jwtUtils.validateJwtToken(token);
        return IntrospectResponse.builder().valid(valid).build();
    }



}
