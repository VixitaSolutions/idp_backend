package com.oversoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String employeeType);

    Role findByIdAndActive(Long id, Boolean active);
}
