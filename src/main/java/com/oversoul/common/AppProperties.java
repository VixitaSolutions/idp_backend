package com.oversoul.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "custom")
@Validated
@Component
@PropertySource({ "classpath:response/messages.properties" })
public class AppProperties {

}
