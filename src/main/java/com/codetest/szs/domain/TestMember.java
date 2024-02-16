package com.codetest.szs.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class TestMember {
    @Id
    @Column(nullable = false)
    private String name;

    private String regNo;
}
