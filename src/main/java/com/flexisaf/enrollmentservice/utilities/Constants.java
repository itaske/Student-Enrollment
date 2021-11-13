package com.flexisaf.enrollmentservice.utilities;


public class Constants {

  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  public static final String LOGIN_URL = "/api/v1/users/login";
  public static final String SIGNUP_URL = "/api/v1/users";
  public static final String ENROLLMENT_URL = "/api/v1/students";
  public static final String FORGOT_PASSWORD = "/api/v1/users/forgot-password";
  public static final String H2_CONSOLE = "/h2-console/**";

  public static final String DEFAULT_EMAIL_SENDER = "udochukwupatric@gmail.com";

  public static final String UNPROTECTED_URLS[] = {
          LOGIN_URL,
          SIGNUP_URL,
          FORGOT_PASSWORD,
          H2_CONSOLE
  };

  public static final String ACTUATOR_URLS = "/actuator/**";

  public static final String SWAGGER_URLS[] = {
    "/v2/api-docs",
    "/swagger-ui/**",
    "/swagger-resources**",
    "/swagger-resources/**",
    "/configuration/**",
    "/swagger-ui.html"
  };

    }

