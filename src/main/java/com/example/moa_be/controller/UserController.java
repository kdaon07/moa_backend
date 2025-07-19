package com.example.moa_be.controller;

import com.example.moa_be.db.Users;
import com.example.moa_be.dto.LoginRequest;
import com.example.moa_be.dto.MyInfoResponse;
import com.example.moa_be.dto.SignupRequest;
import com.example.moa_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.loginAndGetToken(request);
        if (token == null) {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 틀렸습니다.");
        }

        return ResponseEntity.ok(token);
    }

    @GetMapping("/list")
    public List<?> getUsers() {
        return userService.users();
    }

    @GetMapping("/myinfo")
    public MyInfoResponse getMyInfo() {
        return userService.myinfo();
    }
}
