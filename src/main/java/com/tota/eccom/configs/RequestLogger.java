package com.tota.eccom.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RequestLogger implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        long startTime = System.currentTimeMillis();

        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        log.info("[{}] {}{} | IP: {} | Duration: {} ms",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpRequest.getQueryString() != null ? "?" + httpRequest.getQueryString() : "",
                request.getRemoteAddr(),
                duration);
    }

}
