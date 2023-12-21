package com.msara.app.repositories;

import com.msara.app.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByDocument(String document);
    User findUserByEmail(String email);
}
