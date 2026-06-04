package kr.magicbox.search.adapter.in.security.configuration;

import kr.magicbox.search.adapter.in.security.filter.UserInfoExtractFilter;
import kr.magicbox.search.adapter.in.security.properties.TrustedIpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TrustedIpProperties trustedIpProperties;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth.anyExchange().permitAll())
                .build();
    }

    @Bean
    public UserInfoExtractFilter userInfoExtractFilter() {
        return new UserInfoExtractFilter(trustedIpProperties);
    }
}
