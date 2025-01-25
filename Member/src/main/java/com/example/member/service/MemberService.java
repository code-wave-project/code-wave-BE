package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입 처리 (DTO -> Entity 변환 후 저장)
    public void save(@Valid MemberDTO memberDto) {
        Member member = Member.toMemberEntity(memberDto); // DTO를 Entity로 변환
        memberRepository.save(member); // 엔티티 저장
    }

    // 로그인 처리 (이메일과 비밀번호 비교)
    public MemberDTO login(MemberDTO memberDTO) {
        Optional<Member> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail()); // 이메일로 회원 조회
        if (byMemberEmail.isPresent()) { // 회원이 존재하면
            Member member = byMemberEmail.get();
            if (member.getMemberPassword().equals(memberDTO.getMemberPassword())) { // 비밀번호 일치 여부 확인
                return MemberDTO.toMemberDTO(member); // 성공 시 DTO 반환
            } else {
                return null; // 비밀번호 불일치
            }
        } else {
            return null; // 가입된 정보 없음
        }
    }
}
