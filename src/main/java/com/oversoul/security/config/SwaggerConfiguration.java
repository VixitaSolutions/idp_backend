//package com.oversoul.security.config;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.google.common.base.Predicates;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfiguration {
//
//	@Bean
//	public Docket docket() {
//		return new Docket(DocumentationType.SWAGGER_2).securitySchemes(Arrays.asList(apiKey()))
//				.securityContexts(Collections.singletonList(securityContext())).select()
//				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot"))).build()
//				.apiInfo(apiInfo());
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("Admin Backend").description("This api is for backend admin").version("2.2.3")
//				.build();
//	}
//
//	private SecurityContext securityContext() {
//		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
//	}
//
//	private List<SecurityReference> defaultAuth() {
//		final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[] { authorizationScope };
//		return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
//	}
//
//	private ApiKey apiKey() {
//		return new ApiKey("Bearer", "Authorization", "header");
//	}
//
//}
