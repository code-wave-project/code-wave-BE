package com.example.member.repository;

import com.example.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    // 이메일로 회원 정보 조회
    Optional<Member> findByEmail(String email);

    // 사용자명으로 회원 정보 조회
    Optional<Member> findByUsername(String username);

    // 사용자명 또는 이메일로 회원 정보 조회
    Optional<Member> findByUsernameOrEmail(String username, String email);
}
