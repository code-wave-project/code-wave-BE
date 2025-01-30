package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.member.util.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 처리 (DTO -> Entity 변환 후 저장)
    public void save(@Valid MemberDTO memberDto) {
        Member member = Member.toMemberEntity(memberDto); // DTO를 Entity로 변환
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member); // 엔티티 저장
    }

    // 로그인 처리 (username 또는 email로)
    public MemberDTO login(String identifier, String password) {
        Optional<Member> memberOptional = memberRepository.findByUsernameOrEmail(identifier, identifier);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            // 암호화된 비밀번호 검증
            if (passwordEncoder.matches(password, member.getPassword())) {
                return MemberDTO.toMemberDTO(member); // 성공 시 DTO 반환
            }
        }
        return null; // 로그인 실패
    }

    // 비밀번호 재설정 완료
    public boolean resetPassword(String token, String newPassword) {
        try {
            // 토큰 검증 및 이메일 추출
            String email = jwtUtil.validateToken(token);

            Optional<Member> memberOptional = memberRepository.findByEmail(email);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                // 새로운 비밀번호 암호화 및 저장
                member.setPassword(passwordEncoder.encode(newPassword));
                memberRepository.save(member); // 변경 사항 저장
                return true;
            }
        } catch (Exception e) {
            System.out.println("토큰 검증 실패: " + e.getMessage());
        }
        return false; // 실패 시 false 반환
    }

    // 이메일 중복 체크
    public boolean checkEmailDuplicate(String email) {
        return memberRepository.findByEmail(email).isPresent(); // 이메일이 이미 존재하면 true 반환
    }

    // 사용자명 중복 체크
    public boolean checkUsernameDuplicate(String username) {
        return memberRepository.findByUsername(username).isPresent(); // 사용자명이 이미 존재하면 true 반환
    }

    // 비밀번호 재설정 요청
    public boolean sendPasswordResetToken(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            String token = jwtUtil.generatePasswordResetToken(email); // JWT 기반 토큰 생성
            String resetLink = "http://localhost:8080/api/members/reset-password?token=" + token;

            emailService.sendEmail(email, "비밀번호 재설정",
                    "다음 링크를 통해 비밀번호를 재설정하세요:\n" + resetLink);
            return true;
        }
        return false; // 이메일이 등록되지 않은 경우
    }

    public boolean authenticate(String email, String password) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            // 암호화된 비밀번호 검증
            return passwordEncoder.matches(password, member.getPassword());
        }
        return false; // 이메일이 없거나 비밀번호가 일치하지 않으면 false 반환
    }

    // 이메일로 아이디 찾기
    public String findUsernameByEmail(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            // 이메일로 아이디 발송
            Member member = memberOptional.get();
            emailService.sendEmail(email, "아이디 찾기 결과", "회원님의 아이디는 " + member.getUsername() + " 입니다.");
            return member.getUsername();
        }
        return null; // 해당 이메일로 등록된 사용자가 없는 경우
    }
}
