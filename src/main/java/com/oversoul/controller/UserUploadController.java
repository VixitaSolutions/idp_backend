package com.oversoul.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oversoul.exception.CommonException;
import com.oversoul.service.UserUploadService;
import com.oversoul.vo.ApiReturn;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/userUpload/")
@SecurityRequirement(name = "Authorization")
public class UserUploadController {

	private UserUploadService userUploadService;

	public UserUploadController(UserUploadService userUploadService) {
		this.userUploadService = userUploadService;
	}

	@PostMapping(value = "upload/{employeeTypeId}/{tenantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiReturn userUpload(@RequestPart(value = "fileToUpload", required = true) MultipartFile fileToUpload,
			@PathVariable(value = "employeeTypeId", required = true) long employeeTypeId,
			@PathVariable(value = "tenantId", required = true) UUID tenantId) throws IOException, CommonException {
		return userUploadService.processUserUpload(fileToUpload, employeeTypeId, tenantId);
	}

}
