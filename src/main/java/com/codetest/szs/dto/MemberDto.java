package com.codetest.szs.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class MemberDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class memberSignUpRequest {
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
    public static class memberLoginRequest {
        @NotNull
        private String userId;
        @NotNull
        private String password;
    }
}
