package com.example.codewavebe.application;

import com.example.codewavebe.adapter.in.dto.ResetPasswordRequest;
import com.example.codewavebe.adapter.in.dto.SignInRequest;
import com.example.codewavebe.adapter.in.dto.SignInResponse;
import com.example.codewavebe.adapter.in.dto.SignUpRequest;
import com.example.codewavebe.adapter.out.persistence.repository.UserRepository;
import com.example.codewavebe.common.config.EmailClient;
import com.example.codewavebe.common.dto.api.Message;
import com.example.codewavebe.domain.user.Role;
import com.example.codewavebe.domain.user.User;
import com.example.codewavebe.util.helper.JWTUtil;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailClient emailService;
//    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Transactional
    public Message signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.email();
        String password = signUpRequest.password();
        String name = signUpRequest.name();


        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.of(email, password, name);

        userRepository.save(user);

        return Message
                .builder()
                .message("회원가입이 완료되었습니다.")
                .build();
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        String email = signInRequest.email();
        String password = signInRequest.password();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

//        if (password.equals(user.getPassword()) ) {
//            throw new IllegalArgumentException("Wrong password");
//        }

//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                email, password, null);

//        System.out.println("authToken = " + authToken);
//        Authentication authenticate = authenticationManager.authenticate(authToken);

//        SecurityContextHolder.getContext().setAuthentication(authenticate);

//        Role role = user.getRole();
//        System.out.println("role = " + role);

        String accessToken = jwtUtil.createJwt(email, "USER", 36000000000L);

        System.out.println("accessToken = " + accessToken);

        return SignInResponse
                .builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .role(Role.USER)
                .build();
    }

    @Transactional
    public Message forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User with this email does not exist.");
        }

        User user = userOpt.get();
        String resetCode = UUID.randomUUID().toString();
        user.updateCode(resetCode);
        userRepository.save(user);

        emailService.sendOneEmail(user.getUsername(), email, resetCode);

        return Message.builder().message("비밀번호 재설정 코드가 이메일로 전송되었습니다.").build();
    }

    @Transactional
    public Message resetPassword(String email, ResetPasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (email == null) {
            throw new RuntimeException("Invalid email.");
        }

        if (!user.getCode().equals(request.codeNumber())) {
            throw new RuntimeException("Invalid reset code.");
        }

        user.updatePassword(request.newPassword());
        user.updateCode(null);
        userRepository.save(user);

        return Message.builder().message("비밀번호가 성공적으로 변경되었습니다.").build();
    }

}
