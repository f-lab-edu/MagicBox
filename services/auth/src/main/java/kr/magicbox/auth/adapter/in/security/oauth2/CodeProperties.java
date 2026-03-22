package kr.magicbox.auth.adapter.in.security.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "code")
public class CodeProperties {

    private final long ttlSeconds;
}
