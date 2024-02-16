package com.codetest.szs.config;

import com.codetest.szs.common.interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    private final List<String> addEndPointList = Arrays.asList(
            "/szs/scrap",
            "/szs/refund");

    private final List<String> excludePointList = Arrays.asList(
            "/szs/signup",
            "/szs/login");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(addEndPointList)
                .excludePathPatterns(excludePointList);
    }
}
