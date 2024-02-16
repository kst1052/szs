package com.codetest.szs.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class UserDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class userSignUpRequest {
        @NotNull
        private String userId;
        @NotNull
        private String password;
        @NotNull
        private String name;
        @NotNull
        private String regNo;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class userLoginRequest {
        @NotNull
        private String userId;
        @NotNull
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class scrapRequest {
        @NotNull
        private String name;
        @NotNull
        private String regNo;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class taxResponse {
        private String 이름;

        private String 결정세액;
        private String 퇴직연금세액공제;
    }
}
