package kr.magicbox.auth.adapter.in.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.magicbox.auth.adapter.in.security.oauth2.properties.CodeProperties;
import kr.magicbox.auth.adapter.in.security.oauth2.properties.FrontendProperties;
import kr.magicbox.auth.application.dto.result.UserResult;
import kr.magicbox.auth.application.port.out.CodeRepositoryPort;
import kr.magicbox.auth.application.port.out.UserCredentialPort;
import kr.magicbox.auth.domain.aggregate.Code;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserCredentialPort userCredentialPort;
    private final CodeRepositoryPort codeRepositoryPort;
    private final CodeProperties codeProperties;
    private final FrontendProperties frontendProperties;

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2UserInfo userInfo = Objects.requireNonNull(
                (OAuth2UserInfo) authentication.getPrincipal(), "OAuth2 인증 정보가 존재하지 않습니다.");

        // 1. gRPC로 user 서비스 호출
        UserResult userResult = userCredentialPort.loadCredential(
                userInfo.oauth2Id(),
                userInfo.providerType().getProvider(),
                userInfo.email(),
                userInfo.profileImage()
        );

        // 2. 일회용 Code 생성 및 Redis 저장
        String codeValue = UUID.randomUUID().toString();
        Code code = Code.createBuilder()
                .code(codeValue)
                .userId(userResult.userId())
                .role(userResult.userRole())
                .expiresAt(Instant.now().plusSeconds(codeProperties.getTtlSeconds()))
                .build();
        codeRepositoryPort.save(code);

        // 3. 프론트엔드로 리다이렉트
        response.sendRedirect(frontendProperties.getUri() + "/oauth2/callback#code=" + codeValue);
    }
}