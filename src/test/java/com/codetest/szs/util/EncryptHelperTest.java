package com.codetest.szs.util;

import com.codetest.szs.encrypt.EncryptHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class EncryptHelperTest {

    private final EncryptHelper encryptHelper;
    @Autowired
    EncryptHelperTest(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    @Test
    public void bcryptTest() {
        String hash = encryptHelper.encrypt("1234");

        assertThat(hash).hasSize(60);
        assertThat(encryptHelper.isMatch("1234", hash)).isTrue();
    }
}