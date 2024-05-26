package com.tota.eccom.domain.auth.business;

import com.tota.eccom.adapters.dto.auth.LoginRequest;
import com.tota.eccom.adapters.dto.auth.LoginResponse;
import com.tota.eccom.adapters.dto.auth.RefreshRequest;
import com.tota.eccom.domain.auth.IAuthDomain;
import com.tota.eccom.domain.user.IUserDomain;
import com.tota.eccom.security.JwtTokenUtil;
import com.tota.eccom.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthDomain implements IAuthDomain {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserDomain userDomain;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginResponse refresh(RefreshRequest refreshRequest) {
        if (!jwtTokenUtil.isRefreshTokenValid(refreshRequest.getRefreshToken())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtTokenUtil.getUsernameFromToken(refreshRequest.getRefreshToken());
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        String accessToken = jwtTokenUtil.generateToken(userDetails);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshRequest.getRefreshToken()) // Retornando o mesmo refresh token
                .build();
    }
}
