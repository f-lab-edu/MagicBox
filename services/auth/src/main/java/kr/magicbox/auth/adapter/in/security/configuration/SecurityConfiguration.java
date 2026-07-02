package kr.magicbox.auth.adapter.in.security.configuration;

import kr.magicbox.auth.adapter.in.security.filter.UserInfoExtractFilter;
import kr.magicbox.auth.adapter.in.security.oauth2.MagicBoxOAuth2UserService;
import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2LoginFailureHandler;
import kr.magicbox.auth.adapter.in.security.oauth2.OAuth2LoginSuccessHandler;
import kr.magicbox.auth.adapter.in.security.oauth2.properties.CodeProperties;
import kr.magicbox.auth.adapter.in.security.oauth2.properties.FrontendProperties;
import kr.magicbox.auth.adapter.in.web.properties.CookieProperties;
import kr.magicbox.auth.adapter.out.jwt.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({JwtProperties.class, CodeProperties.class, FrontendProperties.class, CookieProperties.class})
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final MagicBoxOAuth2UserService magicBoxOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new UserInfoExtractFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(magicBoxOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler))
                .build();
    }
}
