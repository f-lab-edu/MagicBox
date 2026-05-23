package kr.magicbox.delivery.adapter.out.communication.sweettracker.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "sweet-tracker")
public class SweetTrackerProperties {
    private final String apiKey;
    private final String baseUrl;
    private final int deliveryCompleteLevel;
}
