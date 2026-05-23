package kr.magicbox.payment.adapter.out.toss;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import kr.magicbox.payment.adapter.out.toss.properties.TossPaymentProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@EnableConfigurationProperties(TossPaymentProperties.class)
@RequiredArgsConstructor
public class TossPaymentFeignConfig {

    private final TossPaymentProperties tossPaymentProperties;

    @Bean
    public RequestInterceptor tossBasicAuthInterceptor() {
        String credentials = tossPaymentProperties.getSecretKey() + ":";
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return requestTemplate -> requestTemplate.header("Authorization", "Basic " + encoded);
    }

    @Bean
    public ErrorDecoder tossErrorDecoder() {
        return new TossPaymentErrorDecoder();
    }
}
