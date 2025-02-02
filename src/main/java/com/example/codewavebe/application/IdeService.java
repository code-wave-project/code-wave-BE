package com.example.codewavebe.application;

import com.example.codewavebe.adapter.in.dto.IdeRequestDto;
import com.example.codewavebe.adapter.in.dto.IdeResponseDto;
import com.example.codewavebe.adapter.out.persistence.repository.IdeRepository;
import com.example.codewavebe.adapter.out.persistence.repository.UserRepository;
import com.example.codewavebe.common.dto.api.Message;
import com.example.codewavebe.domain.ide.Ide;
import com.example.codewavebe.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdeService {

    private final IdeRepository ideRepository;
    private final UserRepository userRepository;

    @Transactional
    public IdeResponseDto saveCode(String email, IdeRequestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Ide ide = new Ide(user, requestDto.getCode(), requestDto.getPath());
        Ide savedIde = ideRepository.save(ide);
        return IdeResponseDto.from(savedIde);
    }

    public IdeResponseDto getCode(Long id) {
        Ide ide = ideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
        return IdeResponseDto.from(ide);
    }

    @Transactional
    public Message updateCode(Long id, IdeRequestDto requestDto) {
        Ide ide = ideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
        ide.updateCode(requestDto.getCode());
        return Message.builder().message("해당 소스코드를 수정했습니다.").build();
    }

    @Transactional
    public void deleteCode(Long id) {
        if (!ideRepository.existsById(id)) {
            throw new EntityNotFoundException("Code not found");
        }
        ideRepository.deleteById(id);
    }
}
