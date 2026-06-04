package kr.magicbox.waiting.global.configuration;

import kr.magicbox.waiting.global.properties.WaitingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WaitingProperties.class)
public class WaitingConfiguration {
}
