package com.example.member.entity;

import com.example.member.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // JPA 엔티티
@Setter
@Getter
@Table(name = "member_table") // 테이블명 설정
public class Member {
    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long id;

    @Column(unique = true) // 유일한 이메일
    private String memberEmail;

    @Column // 비밀번호
    private String memberPassword;

    @Column // 회원 이름
    private String memberName;

    // DTO -> Entity 변환
    public static Member toMemberEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setMemberEmail(memberDTO.getMemberEmail());
        member.setMemberPassword(memberDTO.getMemberPassword());
        member.setMemberName(memberDTO.getMemberName());
        return member;
    }
}
