package com.codetest.szs.domain;

import com.codetest.szs.dto.UserDto;
import com.codetest.szs.encrypt.EncryptHelper;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class Member {
    @Id
    private String UserId;

    private String password;

    @Column(nullable = false)
    private String name;

    private String regNo;

    @Column(length = 5000)
    private String incomeInfo;
    private LocalDateTime createDttm;

    private LocalDateTime updateDttm;

    @PrePersist
    private void onPrePersist() {
        if(this.createDttm == null) {
            this.createDttm = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void onPreUpdate() {
        this.updateDttm = LocalDateTime.now();
    }

    public void setIncomeInfo(String incomeInfo) {
        this.incomeInfo = incomeInfo;
    }

    public static Member toEntity(UserDto.userSignUpRequest request) {
        return Member.builder()
                .UserId(request.getUserId())
                .password(request.getPassword())
                .name(request.getName())
                .regNo(request.getRegNo())
                .build();
    }
}
