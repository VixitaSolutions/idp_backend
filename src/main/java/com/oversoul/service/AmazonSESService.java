package com.oversoul.service;

public interface AmazonSESService {
    String sendMail(String email, String s) throws Exception;
}
