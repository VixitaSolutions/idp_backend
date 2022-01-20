package com.oversoul.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	List<Course> findByActive(boolean active);

	Course findByIdAndActive(Long courseId, boolean active);

	List<Course> findByTenantId(UUID tenantId);

	List<Course> findByTenantIdAndActive(UUID tenantId, Boolean active);

}
