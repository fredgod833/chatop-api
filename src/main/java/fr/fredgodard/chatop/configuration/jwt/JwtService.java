package fr.fredgodard.chatop.configuration.jwt;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Slf4j
@Service
public class JwtService {

    private Environment env;

    private final String secret;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public JwtService(Environment env) {
        this.env = env;
        this.secret = env.getProperty("secret.key");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "RSA");
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    public Token generateAccessToken(Client user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getEmail())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        Token result = new Token();
        result.setToken(jwtEncoder.encode(jwtEncoderParameters).getTokenValue());
        return result;
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwt jwt = this.jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            log.error("Invalid Token !", e);
        }
        return false;
    }

    public Jwt readJwt(final String token) {
        return this.jwtDecoder.decode(token);
    }

}
