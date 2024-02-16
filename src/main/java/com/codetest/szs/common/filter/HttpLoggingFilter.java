package com.codetest.szs.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
@Slf4j
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {
    private int maxPayloadLength = 1000;
    public static final String DEFAULT_CHARA_SET = "UTF-8";

    private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
        if (buf == null || buf.length == 0) return "";
        try {
            return new String(buf, 0, buf.length, charsetName);
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        logging(request,response, wrappedRequest, wrappedResponse);
        wrappedResponse.copyBodyToResponse();
    }

    private static final String lineSeparator = System.getProperty("line.separator");
    private void logging(HttpServletRequest request, HttpServletResponse response,
                         ContentCachingRequestWrapper wrappedRequest, ContentCachingResponseWrapper wrappedResponse) throws IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if(request.getRequestURI().contains("/passhc/healthcheck")){
            return;
        }

        if(!("GET".equals(method) && uri.equals("/"))) {
            if (!uri.contains("swagger") && !uri.contains("/images/")) {
                StringBuilder sb = new StringBuilder();
                sb = makeRequestLog(sb, request, wrappedRequest, uri);
                sb = makeResponseLog(sb, request, response, wrappedResponse, uri);
                log.info(sb.toString());
            }
        }
    }

    private StringBuilder makeRequestLog(StringBuilder sb, HttpServletRequest request, ContentCachingRequestWrapper wrappedRequest, String uri) {
        sb.append(lineSeparator).append("========================= Request =========================");
        sb.append(lineSeparator).append("URI      :").append(uri);
        sb.append(lineSeparator).append("Method   :").append(request.getMethod());
        sb.append(lineSeparator).append("Header   :");

        Enumeration names = request.getHeaderNames();
        boolean isFirst = true;
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = request.getHeader(name);
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append(name).append(":").append(value);
        }

        sb.append(lineSeparator).append("body     :");
        Map<String, String[]> paramMap = request.getParameterMap();

        if (!paramMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                sb.append(lineSeparator).append("  # ");
                sb.append(entry.getKey()).append("=");
                String[] values = entry.getValue();
                for (int i = 0; i < values.length; i++) {
                    sb.append(values[i]);
                }
            }
        } else {
            sb.append(this.getContentAsString(wrappedRequest.getContentAsByteArray(), this.maxPayloadLength, DEFAULT_CHARA_SET));
        }
        sb.append(lineSeparator).append("========================= Request End =========================");
        return sb;
    }

    private StringBuilder makeResponseLog(StringBuilder sb, HttpServletRequest request, HttpServletResponse response, ContentCachingResponseWrapper wrappedResponse, String uri) {
        sb.append(lineSeparator).append("========================= Response =========================");
        sb.append(lineSeparator).append("URI      :").append(uri);
        sb.append(lineSeparator).append("Method   :").append(request.getMethod());
        sb.append(lineSeparator).append("Status   :").append(response.getStatus());
        sb.append(lineSeparator).append("body     :");
        byte[] buf = wrappedResponse.getContentAsByteArray();
        sb.append(getContentAsString(buf, this.maxPayloadLength, DEFAULT_CHARA_SET));
        sb.append(lineSeparator).append("========================= Response End =========================\n\n");
        return sb;
    }
}
