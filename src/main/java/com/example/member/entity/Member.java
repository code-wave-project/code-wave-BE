package com.example.member.entity;

import com.example.member.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Integer id; // 사용자 고유 ID

    @Column(name = "username", nullable = false, length = 50)
    private String username; // 아이디

    @Column(name = "email", nullable = false, length = 100)
    private String email; // 이메일 주소

    @Column(name = "password", nullable = false, length = 255)
    private String password; // 암호화된 비밀번호

    // DTO -> Entity 변환
    public static Member toMemberEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setUsername(memberDTO.getUsername()); // 아이디
        member.setEmail(memberDTO.getEmail());       // 이메일 주소
        member.setPassword(memberDTO.getPassword()); // 비밀번호
        return member;
    }
}

