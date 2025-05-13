package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.AuthorUpdateRequest;
import com.example.scheduleapi.entity.Author;
import com.example.scheduleapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;


    public void update(Long id, AuthorUpdateRequest req) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("작성자가 존재하지 않습니다."));

        Author updatedAuthor = new Author(
                author.getId(),
                req.getName(),
                req.getEmail(),
                author.getCreatedAt(),
                LocalDateTime.now()  // 수정일 갱신
        );

        authorRepository.update(updatedAuthor);
    }
}
