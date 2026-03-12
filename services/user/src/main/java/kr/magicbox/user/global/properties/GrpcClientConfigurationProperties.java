package kr.magicbox.user.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.grpc")
public class GrpcClientConfigurationProperties {

    private final Map<String, ServiceConfig> client;

    public record ServiceConfig(String address, String negotiationType, KeepAlive keepAlive) { }

    public record KeepAlive(Duration time, Duration timeout) { }
}
