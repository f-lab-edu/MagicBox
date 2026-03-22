package kr.magicbox.auth.adapter.in.security.oauth2;

import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record OAuth2UserInfo(
        @NonNull String oauth2Id,
        @NonNull String email,
        @NonNull String profileImage,
        @NonNull OAuth2ProviderType providerType,
        @NonNull Map<String, Object> attributes
) implements OAuth2User {

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public @NonNull String getName() {
        return oauth2Id;
    }
}
