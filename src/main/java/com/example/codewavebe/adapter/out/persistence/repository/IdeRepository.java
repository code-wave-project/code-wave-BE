package com.example.codewavebe.adapter.out.persistence.repository;

import com.example.codewavebe.domain.ide.Ide;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeRepository extends JpaRepository<Ide, Long> {
    List<Ide> findByUserId(Long userId);
}
