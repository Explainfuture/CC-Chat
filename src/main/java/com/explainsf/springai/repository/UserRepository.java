package com.explainsf.springai.repository;

import com.explainsf.springai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
