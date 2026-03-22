package kr.magicbox.auth.adapter.in.security.oauth2.strategy;

import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2ProviderType;
import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2Strategy;
import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2UserInfo;
import kr.magicbox.auth.adapter.in.security.oauth2.exception.InvalidOAuth2UserInfoException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoogleOAuth2Strategy implements OAuth2Strategy {

    @Override
    public OAuth2ProviderType getProviderType() {
        return OAuth2ProviderType.GOOGLE;
    }

    @Override
    public OAuth2UserInfo extractUserInfo(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (!(attributes.get("sub") instanceof String oauth2Id)) {
            throw new InvalidOAuth2UserInfoException("oauth2Id");
        }
        if (!(attributes.get("email") instanceof String email)) {
            throw new InvalidOAuth2UserInfoException("email");
        }
        if (!(attributes.get("picture") instanceof String profileImage)) {
            throw new InvalidOAuth2UserInfoException("profileImage");
        }

        return new OAuth2UserInfo(oauth2Id, email, profileImage, OAuth2ProviderType.GOOGLE, attributes);
    }
}