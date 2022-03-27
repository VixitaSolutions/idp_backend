package com.oversoul.controller;

import java.io.IOException;
import java.util.UUID;

import com.oversoul.exception.CommonException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.oversoul.service.CompetencyService;
import com.oversoul.vo.ApiReturn;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/competency/")
@SecurityRequirement(name = "Authorization")
public class CompetencyController {

	private CompetencyService competencyService;

	public CompetencyController(CompetencyService competencyService) {
		this.competencyService = competencyService;
	}

	@GetMapping("all")
	public ApiReturn getCourseTypes(@RequestHeader("tenant-id")UUID tenantId) {
		return competencyService.getCompetencies(tenantId);

	}

	@PostMapping(value = "upload/{tenantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiReturn competencyUpload(@RequestPart(value = "fileToUpload", required = true) MultipartFile fileToUpload,
									  @PathVariable(value = "tenantId", required = true) UUID tenantId, HttpServletRequest request, HttpServletResponse response) throws IOException, CommonException {
		return competencyService.competencyUpload(fileToUpload, tenantId,request,response);
	}

}
