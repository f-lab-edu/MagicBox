package kr.magicbox.auth.adapter.in.security.oauth2.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "frontend")
public class FrontendProperties {

    private final String uri;
}
