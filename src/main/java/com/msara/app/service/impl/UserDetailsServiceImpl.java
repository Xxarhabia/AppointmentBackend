package com.msara.app.service.impl;

import com.msara.app.model.entities.Role;
import com.msara.app.model.entities.UserEntity;
import com.msara.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String sql = "SELECT u.id AS id_usuario, u.name AS nombre_usuario, u.email, u.password, u.document, u.age, u.address, r.name AS nombre_rol " +
                "FROM users u " +
                "JOIN users_roles ur ON u.id = ur.usuario_id " +
                "JOIN role r ON ur.role_id = r.id " +
                "WHERE u.email = ?";

        UserEntity userEntity = jdbcTemplate.queryForObject(sql, new Object[]{username}, (resultSet, i) -> {
            UserEntity user = new UserEntity();
            user.setId(resultSet.getLong("id_usuario"));
            user.setName(resultSet.getString("nombre_usuario"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setDocument(resultSet.getString("document"));
            user.setAge(resultSet.getString("age"));
            user.setAddress(resultSet.getString("address"));

            Role role = new Role();
            role.setName(resultSet.getString("nombre_rol"));

            user.setRoles(Set.of(role));
            return user;
        });

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not fount: " + username);
        }
        
        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName())))
                .collect(Collectors.toSet());

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }
}
