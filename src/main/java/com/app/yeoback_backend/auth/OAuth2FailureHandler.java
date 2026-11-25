package com.app.yeoback_backend.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 소셜 로그인 실패 (사용자가 동의를 취소, 공급자 오류 등)
 * 기본 동작은 백엔드의 /login?error 로 가는데 그런 화면이 없으므로 React 로그인 화면으로 보낸다.
 */
@Component
@Slf4j
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${app.front-url:http://localhost:5173}")
    private String frontUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.warn("oauth login failed", exception);
        response.sendRedirect(frontUrl + "/member/login?oauthError=failed");
    }
}
