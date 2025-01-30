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

    private Integer id; // 사용자 고유 ID

    @Pattern(regexp = "^[a-zA-Z0-9]{4,50}$", message = "사용자 아이디는 4자 이상 50자 이하여야 합니다.")
    private String username; // 사용자 아이디

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email; // 이메일 주소

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
            message = "비밀번호는 특수문자와 영문을 포함하여 8자 이상이어야 합니다.")
    private String password; // 비밀번호

    // Entity -> DTO 변환
    public static MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());           // 사용자 고유 ID
        memberDTO.setUsername(member.getUsername()); // 사용자 아이디
        memberDTO.setEmail(member.getEmail());     // 이메일 주소
        memberDTO.setPassword(member.getPassword()); // 암호화된 비밀번호
        return memberDTO;
    }
}
