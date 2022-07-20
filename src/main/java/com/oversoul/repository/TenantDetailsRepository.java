package com.oversoul.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.TenantDetails;

public interface TenantDetailsRepository extends JpaRepository<TenantDetails, UUID> {
    List<TenantDetails> findByIdAndStatus(UUID id, boolean status);

    List<TenantDetails> findByStatus(Boolean tenantId);

    List<TenantDetails> findByIdAndClientNameLikeIgnoreCase(UUID id, String clientName);

    List<TenantDetails> findByIdAndClientNameLikeIgnoreCaseAndStatus(UUID id, String clientName, boolean status);

    List<TenantDetails> findByClientNameLikeIgnoreCaseAndStatus(String clientName, Boolean status);

    List<TenantDetails> findByClientNameLikeIgnoreCase(String clientName);
    
    Optional<TenantDetails> findByClientNameOrEmail(String clientName, String email);
}
