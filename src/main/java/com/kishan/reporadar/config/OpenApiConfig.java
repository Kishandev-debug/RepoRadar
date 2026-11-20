
package com.kishan.reporadar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI repoRadarOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("RepoRadar API")
                        .description(
                                "GitHub Repository Searcher API. "
                                        + "Search GitHub repositories, save results "
                                        + "to PostgreSQL, and retrieve repositories "
                                        + "using filters and sorting."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RepoRadar")));
    }
}
