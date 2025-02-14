package com.project.hutech_event.service;


import com.project.hutech_event.dto.request.RegisterRequest;
import com.project.hutech_event.dto.request.UserRequest;
import com.project.hutech_event.dto.response.RegisterResponse;
import com.project.hutech_event.dto.response.UserResponse;
import com.project.hutech_event.enums.Gender;
import com.project.hutech_event.model.Role;
import com.project.hutech_event.model.User;
import com.project.hutech_event.repository.RoleRepository;
import com.project.hutech_event.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public RegisterResponse registerUser(RegisterRequest request) {
        try {
            // Kiểm tra username có tồn tại
            if (userRepository.existsByEmail(request.getUsername()) ||
                    userRepository.existsByStudentCode(request.getUsername())) {
                return RegisterResponse.builder()
                        .message("Username đã tồn tại!")
                        .username(request.getUsername())
                        .email(null)  // Thêm email vào response
                        .build();
            }

            // Lấy vai trò USER
            Optional<Role> userRole = roleRepository.findByName("USER");
            if (userRole.isEmpty()) {
                return RegisterResponse.builder()
                        .message("Lỗi hệ thống: Role USER không tồn tại!")
                        .username(request.getUsername())
                        .email(null)  // Thêm email vào response
                        .build();
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // Xử lý email và student code
            String username = request.getUsername();
            if (username.contains("@")) {
                user.setEmail(username);  // Lưu email
            } else {
                user.setStudentCode(username);
                user.setEmail(username + "@temp.hutech.edu.vn");  // Email tạm thời cho student code
            }

            // Thiết lập role
            user.setRoles(new HashSet<>());
            user.getRoles().add(userRole.get());

            // Lưu user và lấy thông tin user đã được lưu
            User savedUser = userRepository.save(user);

            // Trả về response với đầy đủ thông tin
            return RegisterResponse.builder()
                    .message("Đăng ký thành công!")
                    .username(savedUser.getUsername())
                    .email(user.getEmail())
                    .build();

        } catch (Exception e) {
            return RegisterResponse.builder()
                    .message("Đăng ký thất bại: " + e.getMessage())
                    .username(request.getUsername())
                    .email(null)
                    .build();
        }
    }



    // Tạo mới User
    public UserResponse createUser(UserRequest requestDTO) {
        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.findByStudentCode(requestDTO.getStudentCode()).isPresent()) {
            throw new IllegalArgumentException("Student Code already exists");
        }

        User user = new User();
        mapToEntity(requestDTO, user);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword())); // Mã hóa mật khẩu
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }


    // Lấy tất cả Users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Lấy User theo ID
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return mapToDTO(user);
    }

    // Cập nhật User
    public UserResponse updateUser(Long userId, UserRequest requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));


        mapToEntity(requestDTO, user);
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    // Xóa User
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    // Map DTO -> Entity
    private void mapToEntity(UserRequest requestDTO, User user) {
        MultipartFile imageFile = requestDTO.getAvatarUrl();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                if (imageFile.getSize() > 5 * 1024 * 1024) { // 5MB limit
                    throw new RuntimeException("File size exceeds 5MB limit.");
                }
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                user.setAvatarUrl(base64Image); // Cập nhật Base64 image nếu có thay đổi
            } catch (Exception e) {
                throw new RuntimeException("Error processing image file", e);
            }
        }

        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        user.setFullName(requestDTO.getFullName());
        user.setStudentCode(requestDTO.getStudentCode());
        user.setFaculty(requestDTO.getFaculty());
        user.setClazz(requestDTO.getClazz());
        user.setPhoneNumber(requestDTO.getPhoneNumber());
        user.setDateOfBirth(requestDTO.getDateOfBirth());
        user.setGender(requestDTO.getGender());
    }

    // Map Entity -> DTO
    private UserResponse mapToDTO(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getStudentCode(),
                user.getFaculty(),
                user.getClazz(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getLastLogin()
        );
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
