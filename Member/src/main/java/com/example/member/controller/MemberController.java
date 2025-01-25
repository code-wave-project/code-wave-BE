package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService; // 회원 관련 비즈니스 로직을 처리하는 service 클래스

    // 회원가입 페이지를 요청할 때 호출되는 메서드
    @GetMapping("/member/save")
    public String saveForm() {
        return "save"; // save.html 페이지를 반환 (회원가입 폼)
    }

    // 회원가입 처리하는 메서드
    @PostMapping("/member/save")
    public String save(@Valid @ModelAttribute MemberDTO memberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사에서 문제가 있으면 다시 회원가입 페이지로 돌아감
            return "save";
        }
        memberService.save(memberDTO); // 회원가입 정보를 저장하는 서비스 호출
        return "login"; // 회원가입 성공 후 로그인 페이지로 이동
    }

    // 로그인 페이지를 요청할 때 호출되는 메서드
    @GetMapping("/member/login")
    public String loginForm() {
        return "login"; // login.html 페이지를 반환 (로그인 폼)
    }

    // 로그인 처리를 하는 메서드
    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO); // 입력된 회원 정보를 통해 로그인 확인
        if (loginResult != null) {
            // 로그인 성공하면, 세션에 로그인된 사용자의 이메일을 저장
            session.setAttribute("loginResult", loginResult.getMemberEmail());
            return "main"; // 로그인 성공 시 메인 페이지로 이동
        } else {
            // 로그인 실패하면 다시 로그인 페이지로 돌아감
            return "login";
        }
    }

    // 로그아웃 처리 메서드
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션에 저장된 정보를 모두 삭제 (로그아웃)
        return "index"; // 홈 페이지로 돌아감
    }

    // 이메일 중복 체크 처리 메서드
    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail" + memberEmail); // 이메일 중복 체크를 위한 로그 출력
        return "success"; // 이메일 중복 체크 완료 후 성공 메시지 반환
    }
}
