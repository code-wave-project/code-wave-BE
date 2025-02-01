package com.example.codewavebe.application;

import com.example.codewavebe.adapter.in.dto.IdeRequestDto;
import com.example.codewavebe.adapter.in.dto.IdeResponseDto;
import com.example.codewavebe.adapter.out.persistence.repository.IdeRepository;
import com.example.codewavebe.adapter.out.persistence.repository.UserRepository;
import com.example.codewavebe.domain.ide.Ide;
import com.example.codewavebe.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
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
    public IdeResponseDto saveCode(Long userId, IdeRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Ide ide = new Ide(user, requestDto.getCode());
        Ide savedIde = ideRepository.save(ide);
        return IdeResponseDto.from(savedIde);
    }

    public IdeResponseDto getCode(Long id) {
        Ide ide = ideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
        return IdeResponseDto.from(ide);
    }

    public List<IdeResponseDto> getUserCodes(Long userId) {
        List<Ide> ideList = ideRepository.findByUserId(userId);
        return ideList.stream()
                .map(IdeResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public IdeResponseDto updateCode(Long id, IdeRequestDto requestDto) {
        Ide ide = ideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
        ide.updateCode(requestDto.getCode());
        return IdeResponseDto.from(ide);
    }

    @Transactional
    public void deleteCode(Long id) {
        if (!ideRepository.existsById(id)) {
            throw new EntityNotFoundException("Code not found");
        }
        ideRepository.deleteById(id);
    }
}
