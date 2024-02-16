package com.codetest.szs.controller;

import com.codetest.szs.dto.UserDto;
import com.codetest.szs.token.JwtTokenProvider;
import com.codetest.szs.service.ScrapService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class scrapController {
    private final ScrapService scrapService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/szs/scrap")
    public ResponseEntity scrap(@RequestBody UserDto.scrapRequest request) {
        return scrapService.scrap(request);
    }

    @GetMapping("/szs/refund")
    public UserDto.taxResponse refund(HttpServletRequest request) {
        String authToken = jwtTokenProvider.extractJwtTokenFromHeader(request);
        if(jwtTokenProvider.validateToken(authToken)) {
            return scrapService.calculateRefund(jwtTokenProvider.extractClaims(authToken).getSubject());
        } else {
            return null;
        }
    }
}
