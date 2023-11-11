package com.oversoul.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.entity.feedback;
import com.oversoul.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements  FeedbackService {

    private final FeedbackRepository feedbackRepository;

    private final ObjectMapper objectMapper;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, ObjectMapper objectMapper) {
        this.feedbackRepository = feedbackRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public feedback createFeedBack(feedback feedback) {
        return feedbackRepository.save(feedback);
    }

}
