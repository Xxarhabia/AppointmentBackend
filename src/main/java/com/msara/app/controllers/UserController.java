package com.msara.app.controllers;

import com.msara.app.model.dto.UserRequestDTO;
import com.msara.app.repositories.RoleRepository;
import com.msara.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserRequestDTO userRequestDTO) {
        this.userService.addUser(userRequestDTO);
        return ResponseEntity.ok("User added successfully");
    }
}
