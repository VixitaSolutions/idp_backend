package com.oversoul.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.TenantDetails;

public interface TenantDetailsRepository extends JpaRepository<TenantDetails, UUID> {

}
