package kr.magicbox.auth.adapter.in.web.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cookie.refresh-token")
public class CookieProperties {

    private final String name;
    private final Long maxAge;
    private final boolean secure;
    private final String sameSite;
}