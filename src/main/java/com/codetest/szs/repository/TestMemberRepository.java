package com.codetest.szs.repository;

import com.codetest.szs.domain.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMemberRepository extends JpaRepository<TestMember, String> {
}
