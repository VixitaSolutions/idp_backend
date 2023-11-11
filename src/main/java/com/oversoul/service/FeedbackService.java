package com.oversoul.service;

import com.oversoul.entity.feedback;
import com.oversoul.exception.CommonException;

public interface FeedbackService {

    feedback createFeedBack(feedback feedbackReq) throws CommonException;
}
