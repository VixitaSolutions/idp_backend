package com.oversoul.service;

import java.io.IOException;
import java.util.UUID;

import com.oversoul.exception.CommonException;
import com.oversoul.vo.ApiReturn;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CompetencyService {

    ApiReturn getCompetencies(UUID tenantId);

    ApiReturn competencyUpload(MultipartFile fileToUpload, UUID tenantId, HttpServletRequest request, HttpServletResponse response) throws CommonException, IOException;
}
