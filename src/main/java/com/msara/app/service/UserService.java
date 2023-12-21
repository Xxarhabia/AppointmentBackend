package com.msara.app.service;

import com.msara.app.model.dto.UserRequestDTO;
import com.msara.app.model.entities.Role;
import com.msara.app.model.entities.User;
import com.msara.app.repositories.RoleRepository;
import com.msara.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "CLIENT", "EMPLOYEE");

    public void addUser(UserRequestDTO userRequestDTO) {

        User userEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
        User userDocument = userRepository.findUserByDocument(userRequestDTO.getDocument());

        if(userEmail != null) throw new IllegalArgumentException("Email already exists");

        if(userDocument != null) throw new IllegalArgumentException("Document already exists");

        User user = User.builder()
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
