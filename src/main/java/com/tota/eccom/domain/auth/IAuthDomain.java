package com.tota.eccom.domain.auth;

import com.tota.eccom.adapters.dto.auth.LoginRequest;
import com.tota.eccom.adapters.dto.auth.LoginResponse;
import com.tota.eccom.adapters.dto.auth.RefreshRequest;

public interface IAuthDomain {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse refresh(RefreshRequest refreshRequest);
}
