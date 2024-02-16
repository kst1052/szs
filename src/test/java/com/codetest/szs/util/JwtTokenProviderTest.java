package com.codetest.szs.util;

import com.codetest.szs.token.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTokenProviderTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Test
    public void createTokenTest() {
        String jwtToken = jwtTokenProvider.createToken("hong12");

        assertThat(jwtTokenProvider.validateToken(jwtToken)).isTrue();
        assertThat(jwtTokenProvider.extractClaims(jwtToken).getSubject()).isEqualTo("hong12");
    }
}