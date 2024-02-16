package com.codetest.szs.common.interceptor;

import com.codetest.szs.domain.Member;
import com.codetest.szs.repository.MemberRepository;
import com.codetest.szs.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwt;

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = jwt.extractJwtTokenFromHeader(request);

        if (ObjectUtils.isEmpty(token) || !verifyToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String userId = jwt.extractClaims(token).getSubject();

        Optional<Member> member = memberRepository.findById(userId);

        if (ObjectUtils.isEmpty(member)) {
            throw new BadRequestException("등록되지 않은 사용자");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private Boolean verifyToken(String token) {
        return jwt.validateToken(token);
    }
}