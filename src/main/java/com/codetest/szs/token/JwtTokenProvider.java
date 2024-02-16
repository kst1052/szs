package com.codetest.szs.token;

import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    public static final String JWT_PREFIX = "Bearer ";
    private static final long EXPIRE_MINUTES_TOKEN = 1000 * 60L * 60L * 12L; // 12 시간
    @Value("${spring.jwt.secret}")
    private String secret;

    public String createToken(String userId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("neo")
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRE_MINUTES_TOKEN))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims extractClaims(final String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(final String token) {
        return !isTokenExpired(token);
    }
    public String extractJwtTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(JWT_PREFIX)) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}
