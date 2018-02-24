package com.dataprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"com.dataprice"})
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class )
@EnableJpaRepositories({"com.dataprice"})
@EntityScan({"com.dataprice"})
public class SpringBootVaadinHybridMenuApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringBootVaadinHybridMenuApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootVaadinHybridMenuApplication.class, args);
	}
}












