package kr.magicbox.review.adapter.in.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.magicbox.review.adapter.in.security.properties.TrustedIpProperties;
import kr.magicbox.review.domain.vo.UserId;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class UserInfoExtractFilter extends OncePerRequestFilter {

    private final TrustedIpProperties trustedIpProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();

        if (!trustedIpProperties.getIps().contains(clientIp)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userIdHeader = request.getHeader("X-User-Id");

        if (!isValidUserId(userIdHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        UserId userId = UserId.of(Long.valueOf(userIdHeader));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isValidUserId(String userIdHeader) {
        try {
            return Long.parseLong(userIdHeader) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
