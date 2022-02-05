package com.oversoul.service;

import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.CourseReq;

import java.util.UUID;

public interface CourseService {

    ApiReturn createdCourse(CourseReq courseReq);

    ApiReturn getCourseTypes(UUID tenantId, Boolean active);

}
