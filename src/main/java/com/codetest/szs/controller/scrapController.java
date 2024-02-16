package com.codetest.szs.controller;

import com.codetest.szs.dto.ScrapDto;
import com.codetest.szs.token.JwtTokenProvider;
import com.codetest.szs.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class scrapController {
    private final ScrapService scrapService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/szs/scrap")
    @Operation(summary = "스크래핑 요청", description = "스크래핑 API")
    @Parameters({
            @Parameter(name = "name", description = "사용자 이름", example = "홍길동"),
            @Parameter(name = "regNo", description = "사용자 주민번호", example = "860824-1655068")
    })
    public ResponseEntity scrap(@RequestBody ScrapDto.scrapRequest request) {
        return scrapService.scrap(request);
    }

    @GetMapping("/szs/refund")
    @Operation(summary = "결정세액 요청", description = "사용자 결정세액 조회 API")
    public ScrapDto.taxResponse refund(HttpServletRequest request) {
        String authToken = jwtTokenProvider.extractJwtTokenFromHeader(request);
        if(jwtTokenProvider.validateToken(authToken)) {
            return scrapService.calculateRefund(jwtTokenProvider.extractClaims(authToken).getSubject());
        } else {
            return null;
        }
    }
}
