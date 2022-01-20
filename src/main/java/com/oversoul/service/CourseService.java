package com.oversoul.service;

import java.util.UUID;

import com.oversoul.entity.Course;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.CourseReq;

public interface CourseService {

	ApiReturn createdCourse(CourseReq courseReq);

	ApiReturn getCourseTypes(UUID tenantId, Boolean active);

}
