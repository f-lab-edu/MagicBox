package kr.magicbox.auth.adapter.in.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MagicBoxOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2StrategyComposite oauth2StrategyComposite;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2ProviderType providerType = OAuth2ProviderType.of(registrationId);
        return oauth2StrategyComposite.getStrategy(providerType).extractUserInfo(oAuth2User);
    }
}
