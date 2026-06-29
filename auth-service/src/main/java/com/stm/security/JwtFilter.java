/*
 * package com.stm.security;
 * 
 * import java.io.IOException;
 * 
 * import org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.core.userdetails.UserDetailsService; import
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
 * private final JwtUtil jwtUtil; private final UserDetailsService
 * userDetailsService;
 * 
 * @Override protected void doFilterInternal( HttpServletRequest request,
 * HttpServletResponse response, FilterChain filterChain) throws
 * ServletException, IOException {
 * 
 * String path = request.getServletPath();
 * 
 * // Skip public APIs if (path.equals("/auth/login") ||
 * path.equals("/students/login") || path.startsWith("/swagger-ui") ||
 * path.startsWith("/v3/api-docs")) {
 * 
 * filterChain.doFilter(request, response); return; }
 * 
 * String authHeader = request.getHeader("Authorization");
 * 
 * if (authHeader != null && authHeader.startsWith("Bearer ")) { try { String
 * token = authHeader.substring(7);
 * 
 * String username = jwtUtil.extractUsername(token);
 * 
 * if (username != null &&
 * SecurityContextHolder.getContext().getAuthentication() == null) {
 * 
 * var userDetails = userDetailsService.loadUserByUsername(username);
 * 
 * if (jwtUtil.validateToken(token, userDetails)) {
 * 
 * UsernamePasswordAuthenticationToken authentication = new
 * UsernamePasswordAuthenticationToken( userDetails, null,
 * userDetails.getAuthorities() );
 * 
 * SecurityContextHolder.getContext().setAuthentication(authentication); } } }
 * catch (Exception e) { // Invalid token -> ignore and continue } }
 * 
 * filterChain.doFilter(request, response); } }
 */

package com.stm.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip public APIs
        if (path.equals("/admin/login")
                || path.equals("/student/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);

                String username = jwtUtil.extractUsername(token);

                if (username != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    var userDetails =
                            userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(token, userDetails)) {

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }
                }

            } catch (Exception e) {
                // Invalid token
            }
        }

        filterChain.doFilter(request, response);
    }
}