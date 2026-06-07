package com.UCLL.TODO.service;

import com.UCLL.TODO.model.UserDetailsImpl;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class JwtServiceImplementation implements JwtService {
    private final JwtEncoder jwtEncoder;

    public JwtServiceImplementation(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateToken(long id, String email, Collection<String> roles) {
        final var now = Instant.now();
        final var expiresAt = now.plus(30L, ChronoUnit.MINUTES);
        final var header = JwsHeader
                .with(MacAlgorithm.HS256)
                .type("JWT")
                .build();
        final var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(email)
                .claim("email", email)
                .claim("scope", String.join(" ", roles))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public String generateToken(UserDetailsImpl userDetails) {
        return generateToken(
                userDetails.user().getUserId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.toString()).toList());

    }
}
