package com.example.member.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;  // 추가

@Configuration
@ComponentScan("com.example")  // 전체 패키지 스캔 (컨트롤러 찾기 위함)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CodeWave API")
                        .version("1.0")
                        .description("CodeWave 프로젝트의 Swagger API 문서"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth")) // 🔹 API 요청 시 Authorization 헤더 추가
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))); // 🔹 Swagger에 JWT 인증 방식 추가
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("codewave")
                .pathsToMatch("/api/**")  // API 엔드포인트 매칭
                .build();
    }
}
