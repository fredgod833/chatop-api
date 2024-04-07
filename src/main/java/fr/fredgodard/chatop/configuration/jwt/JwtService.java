package fr.fredgodard.chatop.configuration.jwt;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import fr.fredgodard.chatop.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Slf4j
@Service
public class JwtService {

    private static final String SECRET = "7PLTpbz5CRH6WK2m4iphkCY6my5xu1fF";

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public JwtService() {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(), "RSA");
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(SECRET.getBytes()));
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    public String generateAccessToken(Client user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getEmail())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
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
