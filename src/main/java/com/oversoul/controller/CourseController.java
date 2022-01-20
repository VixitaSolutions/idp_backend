package com.oversoul.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.service.CourseService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.CourseReq;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/course/")
@SecurityRequirement(name = "Authorization")
public class CourseController {

	private CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("courseTypes")
	public ApiReturn getCourseTypes(@RequestParam(required = false) UUID tenantId,
			@RequestParam(required = false) Boolean active) {
		return courseService.getCourseTypes(tenantId, active);

	}

	@PostMapping("create")
	public ApiReturn createdCourse(@RequestBody CourseReq courseReq) {
		return courseService.createdCourse(courseReq);
	}

}
