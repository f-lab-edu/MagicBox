package kr.magicbox.search.adapter.in.security.filter;

import kr.magicbox.search.adapter.in.security.properties.TrustedIpProperties;
import kr.magicbox.search.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Optional;

@RequiredArgsConstructor
public class UserInfoExtractFilter implements WebFilter {

    private final TrustedIpProperties trustedIpProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String clientIp = Optional.ofNullable(request.getRemoteAddress())
                .map(InetSocketAddress::getHostString)
                .orElse("");

        if (!trustedIpProperties.getIps().contains(clientIp)) {
            return chain.filter(exchange);
        }

        String userIdHeader = request.getHeaders().getFirst("X-User-Id");
        if (!isValidUserId(userIdHeader)) {
            return chain.filter(exchange);
        }

        UserId userId = UserId.of(Long.valueOf(userIdHeader));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null);

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private boolean isValidUserId(String value) {
        try {
            return Long.parseLong(value) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
