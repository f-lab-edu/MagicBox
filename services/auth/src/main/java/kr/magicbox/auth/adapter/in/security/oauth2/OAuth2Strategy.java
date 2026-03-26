package kr.magicbox.auth.adapter.in.security.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Strategy {
    OAuth2ProviderType getProviderType();
    OAuth2UserInfo extractUserInfo(OAuth2User oAuth2User);
}
