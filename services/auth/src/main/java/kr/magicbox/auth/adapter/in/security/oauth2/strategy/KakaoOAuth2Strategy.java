package kr.magicbox.auth.adapter.in.security.oauth2.strategy;

import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2ProviderType;
import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2Strategy;
import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2UserInfo;
import kr.magicbox.auth.adapter.in.security.oauth2.exception.InvalidOAuth2UserInfoException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoOAuth2Strategy implements OAuth2Strategy {

    @Override
    public OAuth2ProviderType getProviderType() {
        return OAuth2ProviderType.KAKAO;
    }

    @Override
    public OAuth2UserInfo extractUserInfo(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (!(attributes.get("id") instanceof Long kakaoId)) {
            throw new InvalidOAuth2UserInfoException("oauth2Id");
        }
        String oauth2Id = String.valueOf(kakaoId);
        if (!(attributes.get("kakao_account") instanceof Map<?, ?> kakaoAccount)) {
            throw new InvalidOAuth2UserInfoException("kakao_account");
        }
        if (!(kakaoAccount.get("email") instanceof String email)) {
            throw new InvalidOAuth2UserInfoException("email");
        }
        if (!(kakaoAccount.get("profile") instanceof Map<?, ?> profile)
                || !(profile.get("thumbnail_image_url") instanceof String profileImage)) {
            throw new InvalidOAuth2UserInfoException("profileImage");
        }

        return new OAuth2UserInfo(oauth2Id, email, profileImage, OAuth2ProviderType.KAKAO, attributes);
    }
}