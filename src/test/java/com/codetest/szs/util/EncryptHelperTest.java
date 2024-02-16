package com.codetest.szs.util;

import com.codetest.szs.encrypt.EncryptHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EncryptHelperTest {
    @Autowired
    private final EncryptHelper encryptHelper;

    EncryptHelperTest(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    @Test
    public void bcyptTest() {
        String encTest = encryptHelper.encrypt("1234");
        System.out.println("encTest :: " + encTest);
    }
}