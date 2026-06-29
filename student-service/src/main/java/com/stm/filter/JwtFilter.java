/*
 * package com.stm.filter;
 * 
 * import java.io.IOException; import java.util.List;
 * 
 * import org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException;
 * import jakarta.servlet.http.HttpServletRequest; import
 * jakarta.servlet.http.HttpServletResponse; import
 * lombok.RequiredArgsConstructor;
 * 
 * @Component
 * 
 * @RequiredArgsConstructor public class JwtFilter extends OncePerRequestFilter
 * {
 * 
 * private final JwtUtil jwtUtil;
 * 
 * @Override protected void doFilterInternal( HttpServletRequest request,
 * HttpServletResponse response, FilterChain filterChain) throws
 * ServletException, IOException {
 * 
 * String path = request.getServletPath();
 * 
 * // Skip login + swagger if (path.equals("/students/login") ||
 * path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") ||
 * path.equals("/swagger-ui.html")) {
 * 
 * filterChain.doFilter(request, response); return; }
 * 
 * String authHeader = request.getHeader("Authorization");
 * 
 * if (authHeader != null && authHeader.startsWith("Bearer ")) {
 * 
 * String token = authHeader.substring(7);
 * 
 * if (jwtUtil.validateToken(token)) {
 * 
 * String username = jwtUtil.extractUsername(token);
 * 
 * UsernamePasswordAuthenticationToken authentication = new
 * UsernamePasswordAuthenticationToken( username, null, List.of( new
 * SimpleGrantedAuthority("ROLE_STUDENT") ) );
 * 
 * SecurityContextHolder.getContext() .setAuthentication(authentication); } }
 * 
 * filterChain.doFilter(request, response); } }
 */

/*
 * package com.stm.filter;
 * 
 * import java.io.IOException; import java.util.List;
 * 
 * import org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException;
 * import jakarta.servlet.http.HttpServletRequest; import
 * jakarta.servlet.http.HttpServletResponse; import
 * lombok.RequiredArgsConstructor;
 * 
 * @Component
 * 
 * @RequiredArgsConstructor public class JwtFilter extends OncePerRequestFilter
 * {
 * 
 * private final JwtUtil jwtUtil;
 * 
 * @Override protected void doFilterInternal( HttpServletRequest request,
 * HttpServletResponse response, FilterChain filterChain) throws
 * ServletException, IOException {
 * 
 * String path = request.getServletPath();
 * 
 * // Skip public APIs if (path.equals("/login") ||
 * path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") ||
 * path.equals("/swagger-ui.html")) {
 * 
 * filterChain.doFilter(request, response); return; }
 * 
 * String authHeader = request.getHeader("Authorization");
 * 
 * if (authHeader != null && authHeader.startsWith("Bearer ")) {
 * 
 * String token = authHeader.substring(7);
 * 
 * if (jwtUtil.validateToken(token)) {
 * 
 * String username = jwtUtil.extractUsername(token);
 * 
 * UsernamePasswordAuthenticationToken authentication = new
 * UsernamePasswordAuthenticationToken( username, null, List.of( new
 * SimpleGrantedAuthority("ROLE_STUDENT") ) );
 * 
 * SecurityContextHolder.getContext() .setAuthentication(authentication); } }
 * 
 * filterChain.doFilter(request, response); } }
 */

package com.stm.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip public APIs
        if (path.equals("/login")
        		|| path.equals("/validate")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {

                String username = jwtUtil.extractUsername(token);

                // Assign role based on username
                String role = username.equals("admin")
                        ? "ROLE_ADMIN"
                        : "ROLE_STUDENT";

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority(role)
                                )
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}