package com.codetest.szs.service;

import com.codetest.szs.domain.Member;
import com.codetest.szs.dto.UserDto;
import com.codetest.szs.encrypt.EncryptHelper;
import com.codetest.szs.repository.MemberRepository;
import com.codetest.szs.repository.TestMemberRepository;
import com.codetest.szs.token.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.codetest.szs.dto.Response.failure;
import static com.codetest.szs.dto.Response.success;
@Slf4j
@Service
@AllArgsConstructor
public class MemberService {
    private final TestMemberRepository testMemberRepository;
    private final MemberRepository memberRepository;
    private final EncryptHelper encryptHelper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity createMember(UserDto.userSignUpRequest request) {
        if(joinAvailableCheck(request) && !duplicateCheck(request)) {
            request.setPassword(encryptHelper.encrypt(request.getPassword()));
            request.setRegNo(encryptHelper.encrypt(request.getRegNo()));

            memberRepository.save(Member.toEntity(request));
        } else {
            return new ResponseEntity(failure(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(success(), HttpStatus.OK);
    }

    public ResponseEntity login(UserDto.userLoginRequest request) {
        Map<String, String> map = new HashMap<>();

        Optional<Member> member = Optional.of(memberRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalStateException("NO USER")));

        if(validatePassword(request.getPassword(), member.get().getPassword())) {
            String accessToken = jwtTokenProvider.createToken(member.get().getUserId());
            map.put("accessToken", accessToken);
        }

        return new ResponseEntity(success(map), HttpStatus.OK);
    }

    private Boolean joinAvailableCheck(UserDto.userSignUpRequest request) {
        return testMemberRepository.findById(request.getName())
                .filter(member -> member.getName().equals(request.getName()) &&
                        member.getRegNo().equals(request.getRegNo()))
                .isPresent();
    }

    private Boolean duplicateCheck(UserDto.userSignUpRequest request) {
        return memberRepository.findById(request.getUserId())
                .isPresent();
    }
    private Boolean validatePassword(String inputPassword, String savedPassword) {
        return encryptHelper.isMatch(inputPassword, savedPassword);
    }
}
