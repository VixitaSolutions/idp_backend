package com.oversoul.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oversoul.security.model.JwtSettings;

@Service
public class HelperService {

	@Autowired
	private JwtSettings jwtSettings;

	public String getJwtSigningKey() {
		return jwtSettings.getTokenSigningKey();
	}

}
