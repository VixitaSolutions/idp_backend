package com.oversoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
