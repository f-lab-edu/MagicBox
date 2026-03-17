package kr.magicbox.user.global.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "kr.magicbox.user.global.properties")
public class PropertiesConfiguration {
}
