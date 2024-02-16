package com.codetest.szs.controller;

import com.codetest.szs.dto.UserDto;
import com.codetest.szs.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final MemberService memberService;
    @PostMapping("/szs/signup")
    public ResponseEntity signup(@RequestBody UserDto.userSignUpRequest request) {
        return memberService.createMember(request);
    }

    @PostMapping("/szs/login")
    public ResponseEntity login(@RequestBody UserDto.userLoginRequest request) {
        return memberService.login(request);
    }

}
