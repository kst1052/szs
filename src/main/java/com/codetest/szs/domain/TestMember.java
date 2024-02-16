package com.codetest.szs.domain;

import com.codetest.szs.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TestMember {
    @Id
    @Column(nullable = false)
    private String name;

    private String regNo;
}
