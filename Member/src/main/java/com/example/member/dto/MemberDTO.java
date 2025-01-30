package com.example.member.dto;

import com.example.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long id; // 회원 고유 ID

    @Email(message = "유효한 이메일 주소를 입력해주세요.") // 이메일 형식 검증
    private String memberEmail; // 회원 이메일

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$")
    private String memberPassword; // 회원 비밀번호 (특수문자 및 영문 포함 8자 이상)

    private String memberName; // 회원 이름

    // Entity -> DTO 변환
    public static MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setMemberEmail(member.getMemberEmail());
        memberDTO.setMemberPassword(member.getMemberPassword());
        memberDTO.setMemberName(member.getMemberName());
        return memberDTO;
    }
}
