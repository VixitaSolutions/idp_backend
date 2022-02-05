package com.oversoul.service;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.entity.Course;
import com.oversoul.repository.CourseRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import com.oversoul.vo.CourseReq;

@Service
public class CourseServiceImpl implements CourseService {

	private final CourseRepository courseRepo;

	private final ObjectMapper objectMapper;

	public CourseServiceImpl(CourseRepository courseRepo, ObjectMapper objectMapper) {
		this.courseRepo = courseRepo;
		this.objectMapper = objectMapper;
	}

	@Override
	public ApiReturn createdCourse(CourseReq courseReq) {
		Long loggedInUserId = Long.parseLong(MDC.get("userId"));
		Course course = objectMapper.convertValue(courseReq, Course.class);
		course.setCreatedBy(loggedInUserId);
		return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
				courseRepo.save(course).getId());
	}

	@Override
	public ApiReturn getCourseTypes(UUID tenantId, Boolean active) {
		if (tenantId == null || tenantId.toString().trim().length() == 0) {
			if (active == null) {
				return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
						courseRepo.findAll());
			} else {
				return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
						courseRepo.findByActive(active));
			}

		} else {
			if (active == null) {
				return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
						courseRepo.findByTenantId(tenantId));
			} else {
				return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
						courseRepo.findByTenantIdAndActive(tenantId, active));
			}
		}
	}

}
