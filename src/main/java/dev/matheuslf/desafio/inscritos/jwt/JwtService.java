package dev.matheuslf.desafio.inscritos.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "desafio_inscritos_secret_key";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY);
    }

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(getAlgorithm());
    }

    public String extractUsername(String token) {
        return verifyToken(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return verifyToken(token).getExpiresAt().before(new Date());
    }

    private DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }
}

