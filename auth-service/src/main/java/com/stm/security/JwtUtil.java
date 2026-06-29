/*
 * package com.stm.security;
 * 
 * import java.security.Key; import java.util.Date;
 * 
 * import org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.stereotype.Component;
 * 
 * import io.jsonwebtoken.Jwts; import io.jsonwebtoken.security.Keys;
 * 
 * @Component public class JwtUtil {
 * 
 * private final String SECRET = "mysecretkeymysecretkeymysecretkey12345";
 * 
 * private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
 * 
 * // Generate token public String generateToken(String username) { return
 * Jwts.builder() .setSubject(username) .issuedAt(new Date()) .expiration(new
 * Date(System.currentTimeMillis() + 86400000)) .signWith(key) .compact(); }
 * 
 * // Extract username public String extractUsername(String token) { return
 * Jwts.parser() .verifyWith((javax.crypto.SecretKey) key) .build()
 * .parseSignedClaims(token) .getPayload() .getSubject(); }
 * 
 * // Validate token public boolean validateToken(String token, UserDetails
 * userDetails) { final String username = extractUsername(token);
 * 
 * return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
 * }
 * 
 * // Check expiration private boolean isTokenExpired(String token) { Date
 * expiration = Jwts.parser() .verifyWith((javax.crypto.SecretKey) key) .build()
 * .parseSignedClaims(token) .getPayload() .getExpiration();
 * 
 * return expiration.before(new Date()); } }
 */

package com.stm.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey12345";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate token
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 86400000)
                )
                .signWith((SecretKey) key)
                .compact();
    }

    // Extract username
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validate token
    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // Check token expiration
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