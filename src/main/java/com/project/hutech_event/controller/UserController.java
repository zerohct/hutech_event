package com.project.hutech_event.controller;


import com.project.hutech_event.dto.request.UserRequest;
import com.project.hutech_event.dto.response.UserResponse;
import com.project.hutech_event.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    @Autowired
    private UserService userService;

    // Tạo mới User
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@ModelAttribute @Valid UserRequest requestDTO) {
        UserResponse response = userService.createUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lấy tất cả Users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    // Lấy User theo ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    // Cập nhật User
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @ModelAttribute @Valid UserRequest requestDTO) {
        UserResponse response = userService.updateUser(userId, requestDTO);
        return ResponseEntity.ok(response);
    }

    // Xóa User
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
