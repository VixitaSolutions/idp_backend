package com.oversoul.security.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "auth.security")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtSettings {

	private Integer tokenExpirationTime;

	private String tokenIssuer;

	private String tokenSigningKey;

	private Integer refreshTokenExpTime;

}
