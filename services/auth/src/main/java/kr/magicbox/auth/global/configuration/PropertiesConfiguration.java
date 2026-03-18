package kr.magicbox.auth.global.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "kr.magicbox.auth.global.properties")
public class PropertiesConfiguration {
}