package com.codetest.szs.service;

import com.codetest.szs.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ScrapServiceTest {
    @Autowired
    private ScrapService scrapService;

    @Test
    public void scrapServiceTest() throws IOException {
        UserDto.scrapRequest request = new UserDto.scrapRequest("홍길동", "860824-1655068");
        scrapService.scrap(request);
    }
}