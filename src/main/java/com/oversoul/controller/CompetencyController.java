package com.oversoul.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.service.CompetencyService;
import com.oversoul.vo.ApiReturn;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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

}
