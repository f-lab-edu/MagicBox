package kr.magicbox.auth.adapter.in.security.oauth2;

import kr.magicbox.auth.adapter.in.security.oauth2.exception.UnsupportedOAuth2ProviderException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class OAuth2StrategyComposite {
    private final Map<OAuth2ProviderType, OAuth2Strategy> strategyMap;

    public OAuth2StrategyComposite(Set<OAuth2Strategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(toMap(OAuth2Strategy::getProviderType, identity()));
    }

    public OAuth2Strategy getStrategy(OAuth2ProviderType providerType) {
        return Optional.ofNullable(strategyMap.get(providerType))
                .orElseThrow(() -> new UnsupportedOAuth2ProviderException(providerType.name()));
    }
}
