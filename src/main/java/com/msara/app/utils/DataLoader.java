package com.msara.app.utils;

import com.msara.app.model.entities.Role;
import com.msara.app.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            log.info("Loading role data to database...");
            roleRepository.saveAll(
                    List.of(
                            Role.builder().name("ADMIN").build(),
                            Role.builder().name("CLIENT").build(),
                            Role.builder().name("EMPLOYEE").build()
                    )
            );
            log.info("Roles data has been loaded successfully");
        }
    }
}
