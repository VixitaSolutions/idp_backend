package com.oversoul.controller;

import com.oversoul.service.AmazonSESService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class HealthCheckController {

    @Autowired
    public AmazonSESService amazonSESService;

    @GetMapping("health-check")
    public String healthCheck() {
        return "it is up";
    }

    @GetMapping("mail")
    public String testMail(@RequestParam String email) throws Exception {

        Integer otp = (int) (Math.floor(100000 + Math.random() * 900000));
        return amazonSESService.sendMail(email, formEmailContent(otp));
    }

    private String formEmailContent(Integer otp) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("/email_content/sign_up_otp.html");
            InputStream inputStream = classPathResource.getInputStream();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();

            // Set button click URL
            content = content.replace("SignUpOtp", otp.toString());

            return content;
        } catch (Exception e) {
            log.error("Error reading verification email content: ", e);
        }
        return "";
    }

}
