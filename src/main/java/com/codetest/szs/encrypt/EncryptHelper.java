package com.codetest.szs.encrypt;
public interface EncryptHelper {
    String encrypt(String password);
    boolean isMatch(String password, String hashed);
}
