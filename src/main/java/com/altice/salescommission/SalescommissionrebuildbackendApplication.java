package com.altice.salescommission;

import java.util.Arrays;
import java.util.Collections;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "COMPAS API's", version = "1.0", description = "COMPAS API's Complete Guide"))
public class SalescommissionrebuildbackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(SalescommissionrebuildbackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SalescommissionrebuildbackendApplication.class, args);
	}

	@Autowired
	private Environment environment;

	@Bean
	public CorsFilter corsFilter() {

		String[] activeProfiles = environment.getActiveProfiles();
		logger.info("active profiles: " + Arrays.toString(activeProfiles));

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);

		if (activeProfiles[0].equals("dev")) {
			logger.info("Active Profile: " + activeProfiles[0]);
			corsConfiguration.setAllowedOrigins(
					Collections.singletonList("https://salescommissionrebuildfrontend-exfhn7j3kq-ue.a.run.app"));
		} else if (activeProfiles[0].equals("uat")) {
			logger.info("Active Profile: " + activeProfiles[0]);
			corsConfiguration.setAllowedOrigins(
					Collections.singletonList("https://compas-uat-frontend-exfhn7j3kq-ue.a.run.app"));
		} else if (activeProfiles[0].equals("prod")) {
			logger.info("Active Profile: " + activeProfiles[0]);
			corsConfiguration.setAllowedOrigins(
					Collections.singletonList("https://compas-prod-frontend-sf3kqgr7va-ue.a.run.app"));
		} else if (activeProfiles[0].equals("uatusrlocal")) {
			logger.info("Active Profile: " + activeProfiles[0]);
			corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		} else if (activeProfiles[0].equals("uatmgrlocal")) {
			logger.info("Active Profile: " + activeProfiles[0]);
			corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		} else if (activeProfiles[0].equals("prodlocal")) {
			logger.info("Active Profile: " + activeProfiles[0]);
			corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		} else {
			logger.info("Active Profile: " + activeProfiles[0]);
			corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		}

		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token",
				"Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
				"Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
	
	@PostConstruct
    public void init(){
      // Setting Spring Boot SetTimeZone
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
