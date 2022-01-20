package com.oversoul.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.oversoul.exception.CommonException;
import com.oversoul.vo.ApiReturn;

public interface UserUploadService {

	ApiReturn processUserUpload(MultipartFile fileToUpload, long employeeTypeId, UUID tenantId) throws IOException, CommonException;

}
