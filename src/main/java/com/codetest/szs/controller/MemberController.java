package com.codetest.szs.controller;

import com.codetest.szs.dto.MemberDto;
import com.codetest.szs.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/szs/signup")
    @Operation(summary = "회원가입", description = "회원 가입 API")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 아이디", example = "hong12"),
            @Parameter(name = "password", description = "사용자 비밀번호", example = "123456"),
            @Parameter(name = "name", description = "사용자 이름", example = "홍길동"),
            @Parameter(name = "regNo", description = "사용자 주민번호", example = "860824-1655068")
    })
    public ResponseEntity signup(@RequestBody MemberDto.memberSignUpRequest request) {
        return memberService.createMember(request);
    }

    @PostMapping("/szs/login")
    @Operation(summary = "로그인", description = "회원 로그인 API")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 아이디", example = "hong12"),
            @Parameter(name = "password", description = "사용자 비밀번호", example = "123456")
    })
    public ResponseEntity login(@RequestBody MemberDto.memberLoginRequest request) {
        return memberService.login(request);
    }

}
