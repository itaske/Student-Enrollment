package com.flexisaf.enrollmentservice.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerDocConfig {

    @Bean
    public Docket api(){
       return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .apiInfo(getAPiInfo());
    }


    private ApiInfo getAPiInfo() {
        return new ApiInfo(
                "Student Enrollment Backend API",
                "API that is used in management of Student Enrollment.",
                "Version 1",
                "Terms of service",
                new Contact("Udochukwu Patrick", "https://www.github.com/itaske", "udochukwupatric@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
