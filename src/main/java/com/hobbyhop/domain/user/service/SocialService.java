package com.hobbyhop.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;

public interface SocialService {
    public void socialLogin(String code, HttpServletResponse response);
}