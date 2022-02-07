package com.oversoul.repository;

import com.oversoul.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String encrypt);

    Boolean existsByEmail(String email);

    @Query("select u.id from User u where u.email = :email")
    Long findIdByEmail(@Param("email") String email);

    List<User> findByIdIn(List<Long> employeeIds);

    Boolean existsByEmailAndTenantId(String email, UUID tenantId);

    User findByEmailAndTenantId(String encrypt, UUID tenantId);

    List<User> findByTenantId(UUID tenantId);

    @Override
    boolean existsById(Long aLong);

    @Query(value = "select u from User u join  UserRole ur ON ur.userId=u.id where u.tenantId = :tenantId and ur.roleId.id = :roleId")
    List<User> findByTenantIdAndRoleId_Id(UUID tenantId, Long roleId);

    @Query(value = "select u from User u join  UserRole ur ON ur.userId=u.id where ur.roleId.id=:roleId")
    List<User> findByRoleId_Id(Long roleId);
}
