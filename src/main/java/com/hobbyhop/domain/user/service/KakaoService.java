package com.hobbyhop.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;

public interface KakaoService {
    public void kakaoLogin(String code, HttpServletResponse response);
}