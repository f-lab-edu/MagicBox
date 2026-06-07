package kr.magicbox.auth.adapter.in.web;

import kr.magicbox.auth.adapter.out.jwt.JwtTokenManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
public class JwksController {

    private final JwtTokenManager jwtTokenManager;

    public JwksController(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        RSAPublicKey rsaPublicKey = jwtTokenManager.getPublicKey();

        String n = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(rsaPublicKey.getModulus().toByteArray());
        String e = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(rsaPublicKey.getPublicExponent().toByteArray());

        Map<String, Object> jwk = Map.of(
                "kty", "RSA",
                "use", "sig",
                "alg", "RS256",
                "kid", "magicbox-auth-key",
                "n", n,
                "e", e
        );

        return Map.of("keys", List.of(jwk));
    }
}
