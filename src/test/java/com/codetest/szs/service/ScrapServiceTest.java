package com.codetest.szs.service;

import com.codetest.szs.dto.MemberDto;
import com.codetest.szs.dto.ScrapDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@SpringBootTest
class ScrapServiceTest {
    @Autowired
    private ScrapService scrapService;

    @Test
    public void scrapServiceTest() throws IOException {
        ScrapDto.scrapRequest request = new ScrapDto.scrapRequest("홍길동", "860824-1655068");
        ResponseEntity response = scrapService.scrap(request);

        assertThat(response.getBody()).isNotNull();
    }
}