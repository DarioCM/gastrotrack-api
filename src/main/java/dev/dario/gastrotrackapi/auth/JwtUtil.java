package dev.dario.gastrotrackapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;


@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key key;

    // TODO: change this to a database or cache REDIS
    private Set<String> invalidatedTokens = new HashSet<>();

    @PostConstruct
    public void init() {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            throw new IllegalStateException("SECRET_KEY must not be null or empty");
        }
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 1; // 1 hour

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        //adding role
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList()); // Ensure roles are added

        return createToken(claims, userDetails.getUsername());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !invalidatedTokens.contains(token);
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Extract roles
    }

    public void invalidateToken(String token) {
        // Implement token invalidation
        invalidatedTokens.add(token);
    }
}
