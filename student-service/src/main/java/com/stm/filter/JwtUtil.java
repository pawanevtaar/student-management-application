/*
 * package com.stm.filter;
 * 
 * import java.security.Key; import java.util.Date;
 * 
 * import javax.crypto.SecretKey;
 * 
 * import org.springframework.stereotype.Component;
 * 
 * import io.jsonwebtoken.Jwts; import io.jsonwebtoken.security.Keys;
 * 
 * @Component public class JwtUtil {
 * 
 * private final String SECRET = "mysecretkeymysecretkeymysecretkey12345";
 * 
 * private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
 * 
 * public String extractUsername(String token) { return Jwts.parser()
 * .verifyWith((SecretKey) key) .build() .parseSignedClaims(token) .getPayload()
 * .getSubject(); } public String generateToken(String username) { return
 * Jwts.builder() .setSubject(username) .setIssuedAt(new Date())
 * .setExpiration(new Date(System.currentTimeMillis() + 86400000))
 * .signWith(key) .compact(); } public boolean validateToken(String token) { try
 * { extractUsername(token); return true; } catch (Exception e) { return false;
 * } } }
 */

package com.stm.filter;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET =
            "mysecretkeymysecretkeymysecretkey12345";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // Extract username
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Generate token (optional here, mainly auth-service uses it)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            extractUsername(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Check expiration
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }
}