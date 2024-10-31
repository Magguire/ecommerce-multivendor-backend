package com.eshop.repository;

import com.eshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { // <Entity name, Primary Key Datatype>

    User findByEmail(String email);
}
