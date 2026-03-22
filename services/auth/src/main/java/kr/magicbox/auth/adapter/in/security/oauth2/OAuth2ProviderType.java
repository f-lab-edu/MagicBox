package kr.magicbox.auth.adapter.in.security.oauth2;

import kr.magicbox.auth.adapter.in.security.oauth2.exception.UnsupportedOAuth2ProviderException;
import kr.magicbox.auth.grpc.user.GrpcOAuth2Provider;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum OAuth2ProviderType {
    GOOGLE("google", GrpcOAuth2Provider.GOOGLE),
    KAKAO("kakao", GrpcOAuth2Provider.KAKAO),
    NAVER("naver", GrpcOAuth2Provider.NAVER);

    private final String registrationId;
    @Getter
    private final GrpcOAuth2Provider provider;

    OAuth2ProviderType(String registrationId, GrpcOAuth2Provider provider) {
        this.registrationId = registrationId;
        this.provider = provider;
    }

    private static final Map<String, OAuth2ProviderType> REGISTRATION_ID_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(t -> t.registrationId, t -> t));

    public static OAuth2ProviderType of(String registrationId) {
        OAuth2ProviderType type = REGISTRATION_ID_MAP.get(registrationId.toLowerCase());
        if (type == null) {
            throw new UnsupportedOAuth2ProviderException(registrationId);
        }
        return type;
    }
}
