package com.oversoul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;

import com.oversoul.common.ChatLocaleResolver;

@SpringBootApplication
public class LsmOversoulApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LsmOversoulApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LsmOversoulApplication.class);
	}

	@Bean
	PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new ChatLocaleResolver();
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("response/messages"); // name of the resource bundle
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}

}
