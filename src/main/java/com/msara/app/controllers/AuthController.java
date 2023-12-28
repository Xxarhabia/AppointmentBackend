package com.msara.app.controllers;

import com.msara.app.model.dto.UserRequestDTO;
import com.msara.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> singup(@RequestBody UserRequestDTO userRequestDTO) {
        authService.singup(userRequestDTO);
        return ResponseEntity.ok("Account created");
    }

}
