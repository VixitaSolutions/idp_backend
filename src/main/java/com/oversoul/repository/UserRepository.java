package com.oversoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String encrypt);

}
