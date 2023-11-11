package com.oversoul.controller;

import com.oversoul.exception.CommonException;
import com.oversoul.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import com.oversoul.entity.feedback;

@RestController
@RequestMapping("/api/v1/feedback/")
@SecurityRequirement(name = "Authorization")
public class FeedbackController {

	private final FeedbackService feedbackService;

	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@PostMapping("/createFeedback") //Insert
	public feedback saveFeedback(@RequestBody feedback feedback) throws CommonException {
		return feedbackService.createFeedBack(feedback);
	}

}
