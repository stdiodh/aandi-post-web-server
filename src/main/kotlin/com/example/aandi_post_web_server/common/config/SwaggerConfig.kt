package com.example.aandi_post_web_server.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openApi() : OpenAPI = OpenAPI()
        .components(Components())
        .info(swaggerInfo())
        .addServersItem(Server().url("/"))

    private fun swaggerInfo() : Info = Info()
        .title("A&I 3기 과제 공지용 웹서버")
        .description("A&I 3기 과제 공지를 하기 위한 웹서버 Api 명세서입니다.")
        .version("1.0.0")
}