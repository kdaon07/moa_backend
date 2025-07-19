package com.example.moa_be.service;

import com.example.moa_be.db.Users;
import com.example.moa_be.dto.LoginRequest;
import com.example.moa_be.dto.MyInfoResponse;
import com.example.moa_be.dto.SignupRequest;
import com.example.moa_be.repository.UserRepository;
import com.example.moa_be.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public String signup(SignupRequest request) {
        if (userRepository.findByUserid(request.getUserid()).isPresent()) {
            return "이미 존재하는 아이디입니다.";
        }

        Users user = new Users();
        user.setUserid(request.getUserid());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setSchool(request.getSchool());
        user.setStudentNumber(request.getStudentNumber());
        user.setRole(request.getRole());

        userRepository.save(user);
        return "회원가입 성공!";
    }

    // JWT 발급
    public String loginAndGetToken(LoginRequest request) {
        Users user = userRepository.findByUserid(request.getUserid()).orElse(null);
        if (user == null) {
            return null;
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return null;
        }

        return jwtTokenProvider.createToken(user.getUserid(), user.getRole());
    }
    public List<Users> users() {
        return userRepository.findAll();
    }

    public MyInfoResponse myinfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        Users user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new MyInfoResponse(
                user.getUsername(),
                user.getSchool(),
                user.getStudentNumber(),
                user.getRole()
        );
    }
}
