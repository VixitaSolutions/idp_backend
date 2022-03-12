package com.oversoul.repository;

import com.oversoul.entity.User;
import com.oversoul.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String encrypt);

    Boolean existsByEmail(String email);

    @Query("select u.id from User u where u.email = :email")
    Long findIdByEmail(@Param("email") String email);

    @Query("select u from User u where u.email = :email and u.tenantId = (select t.id from TenantDetails t where t.clientName = :tenant)")
    User findIdByEmailAndTenant(@Param("email") String email, @Param("tenant") String tenant);

    List<User> findByIdIn(List<Long> employeeIds);

    Boolean existsByEmailAndTenantId(String email, UUID tenantId);

    User findByEmailAndTenantId(String encrypt, UUID tenantId);

    @Query(value = "select u.id as id,u.email as email ,u.firstName as firstName,u.lastName as lastName,u.userName as userName,u.createdOn as createdOn,u.mobile as mobile,u.tenantId as tenantId,u.active as active,t.tenantName as tenantName from User u join  UserRole ur ON ur.userId=u.id join TenantDetails t ON t.id=u.tenantId where u.tenantId = :tenantId ")
    List<UserProjection> findByTenantId(@Param("tenantId") UUID tenantId);

    @Override
    boolean existsById(Long aLong);

    @Query(value = "select u.id as id,u.email as email ,u.firstName as firstName,u.lastName as lastName,u.userName as userName,u.createdOn as createdOn,u.mobile as mobile,u.tenantId as tenantId,u.active as active,t.tenantName as tenantName  from User u join  UserRole ur ON ur.userId=u.id join TenantDetails t ON t.id=u.tenantId where u.tenantId = :tenantId and ur.roleId.id = :roleId")
    List<UserProjection> findByTenantIdAndRoleId_Id(@Param("tenantId") UUID tenantId, @Param("roleId") Long roleId);

    @Query(value = "select u.id as id,u.email as email ,u.firstName as firstName,u.lastName as lastName,u.userName as userName,u.createdOn as createdOn,u.mobile as mobile,u.tenantId as tenantId,u.active as active,t.tenantName as tenantName  from User u join  UserRole ur ON ur.userId=u.id join TenantDetails t ON t.id=u.tenantId where ur.roleId.id=:roleId")
    List<UserProjection> findByRoleId_Id(@Param("roleId") Long roleId);

    @Query(value = "select u from User u join  UserRole ur ON ur.userId=u.id where u.tenantId = :tenantId")
    List<User> findByTenantIdWithOutRole(@Param("tenantId") UUID tenantId);

    @Query(value = "select u.id from User u  where u.tenantId = :tenantId")
    List<Long> findIdByTenantId(@Param("tenantId") UUID tenantId);

    List<User> findByIdNotInAndTenantId(Collection<Long> ids, UUID tenantId);

    @Query(value = "select u.id as id,u.email as email ,u.firstName as firstName,u.lastName as lastName,u.userName as userName,u.createdOn as createdOn,u.mobile as mobile,u.tenantId as tenantId,u.active as active,t.tenantName as tenantName from User u join  UserRole ur ON ur.userId=u.id join TenantDetails t ON t.id=u.tenantId ")
    List<UserProjection> findAllWithTenantName();
}
