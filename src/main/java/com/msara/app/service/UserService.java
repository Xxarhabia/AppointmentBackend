package com.msara.app.service;

import com.msara.app.model.dto.UserRequestDTO;
import com.msara.app.model.entities.Role;
import com.msara.app.model.entities.UserEntity;
import com.msara.app.repositories.RoleRepository;
import com.msara.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(UserRequestDTO userRequestDTO) {

        UserEntity userEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
        UserEntity userDocument = userRepository.findUserByDocument(userRequestDTO.getDocument());

        if(userEmail != null) throw new IllegalArgumentException("Email already exists");

        if(userDocument != null) throw new IllegalArgumentException("Document already exists");

        UserEntity user = UserEntity.builder()
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .document(userRequestDTO.getDocument())
                .age(userRequestDTO.getAge())
                .address(userRequestDTO.getAddress())
                .roles(new HashSet<>())
                .build();

        for (Role role : userRequestDTO.getRoles()) {
            Optional<Role> existingRole = roleRepository.findByName(role.getName());
            if (existingRole.isPresent()) {
                user.getRoles().add(existingRole.get());
            } else {
                throw new IllegalArgumentException("Role not found: " + role.getName());
            }
        }

        userRepository.save(user);
        log.info("User added: {}", user);
    }

}
