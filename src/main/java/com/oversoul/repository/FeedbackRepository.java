package com.oversoul.repository;

import com.oversoul.entity.feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<feedback, String>{
}
