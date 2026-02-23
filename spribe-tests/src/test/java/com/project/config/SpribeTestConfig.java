package com.project.config;

import com.project.restapi.TafRestApiConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TafRestApiConfiguration.class)
public class SpribeTestConfig {
}
