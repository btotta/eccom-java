package com.tota.eccom.security;

import com.tota.eccom.util.InvalidJwtTokenUtil;
import com.tota.eccom.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final UserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(AUTH_HEADER);
        String username = null;
        String jwtToken = null;

        if (isTokenPresentAndValid(requestTokenHeader)) {
            jwtToken = requestTokenHeader.substring(TOKEN_PREFIX.length());
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            validateAndSetAuthentication(request, jwtToken, username);
        }

        chain.doFilter(request, response);
    }

    private boolean isTokenPresentAndValid(String requestTokenHeader) {
        return requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)
                && !InvalidJwtTokenUtil.isTokenInvalid(requestTokenHeader);
    }

    private void validateAndSetAuthentication(HttpServletRequest request, String jwtToken, String username) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken, userDetails))) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
