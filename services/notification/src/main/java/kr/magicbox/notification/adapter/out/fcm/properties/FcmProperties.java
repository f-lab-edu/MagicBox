package kr.magicbox.notification.adapter.out.fcm.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "fcm")
public class FcmProperties {
    private final String serviceAccountJson;
}
