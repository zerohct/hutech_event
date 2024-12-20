package com.project.hutech_event.service;


import com.project.hutech_event.dto.request.RegisterRequest;
import com.project.hutech_event.dto.response.RegisterResponse;
import com.project.hutech_event.enums.Gender;
import com.project.hutech_event.model.Role;
import com.project.hutech_event.model.User;
import com.project.hutech_event.repository.RoleRepository;
import com.project.hutech_event.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterResponse registerUser(RegisterRequest request) {
        // Kiểm tra username hoặc email đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            return  RegisterResponse.builder().message("Username đã tồn tại!")
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .build();
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return  RegisterResponse.builder().message("Email đã tồn tại!!!")
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .build();
        }

        // Lấy vai trò USER từ cơ sở dữ liệu
        Optional<Role> userRole = roleRepository.findByName("USER");
        if (userRole.isEmpty()) {
            throw new RuntimeException("Role USER không tồn tại trong cơ sở dữ liệu!");
        }

        // Tạo đối tượng User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setStudentCode(request.getStudentCode());
        user.setFaculty(request.getFaculty());
        user.setClazz(request.getClazz());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth())); // Chuyển chuỗi thành LocalDate
        user.setGender(mapGender(request.getGender())); // Chuyển chuỗi thành enum
        user.setRoles(new HashSet<>());
        user.getRoles().add(userRole.get()); // Gán vai trò USER

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);

        return  RegisterResponse.builder().message("Đăng ký thành công!!!")
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private Gender mapGender(String gender) {
        switch (gender.toLowerCase()) {
            case "nam":
                return Gender.Nam;
            case "nữ":
            case "nu":
                return Gender.Nữ;
            case "khác":
            case "khac":
                return Gender.Khác;
            default:
                throw new IllegalArgumentException("Giới tính không hợp lệ: " + gender);
        }
    }

}
