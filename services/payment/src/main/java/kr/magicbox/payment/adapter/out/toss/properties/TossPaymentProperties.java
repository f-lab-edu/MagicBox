package kr.magicbox.payment.adapter.out.toss.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "toss.payment")
public class TossPaymentProperties {
    private final String secretKey;
    private final String baseUrl;
}
