package kr.magicbox.media.adapter.out.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private final String endpoint;
    private final String publicEndpoint;
    private final String accessKey;
    private final String secretKey;
    private final String bucket;
    private final long presignedUrlTtlSeconds;
}
