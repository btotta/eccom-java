package com.tota.eccom.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tota.eccom.domain.common.view.Views;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;

@ControllerAdvice
public class RoleBasedJsonViewAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;
    private final ObjectWriter publicViewWriter;
    private final ObjectWriter internalViewWriter;

    public RoleBasedJsonViewAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.publicViewWriter = objectMapper.writerWithView(Views.Public.class);
        this.internalViewWriter = objectMapper.writerWithView(Views.Internal.class);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && body != null) {
            ObjectWriter writer = chooseWriterBasedOnRole(authentication);
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                byte[] jsonBytes = writer.writeValueAsBytes(body);
                response.getBody().write(jsonBytes);
                return null;
            } catch (IOException e) {
                throw new RuntimeException("Error writing JSON output", e);
            }
        }
        return body;
    }

    private ObjectWriter chooseWriterBasedOnRole(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return internalViewWriter;
        } else {
            return publicViewWriter;
        }
    }
}