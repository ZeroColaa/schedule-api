package com.example.scheduleapi.controller;

import com.example.scheduleapi.dto.AuthorUpdateRequest;
import com.example.scheduleapi.entity.Author;
import com.example.scheduleapi.dto.AuthorCreateRequest;
import com.example.scheduleapi.repository.AuthorRepository;
import com.example.scheduleapi.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody AuthorCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Author author = new Author(
                null,
                request.getName(),
                request.getEmail(),
                now,
                now
        );

        Long id = authorRepository.save(author);

        Author saved = new Author(
                id,
                author.getName(),
                author.getEmail(),
                author.getCreatedAt(),
                author.getModifiedAt()
        );

        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public void updateAuthor(@PathVariable Long id, @RequestBody AuthorUpdateRequest request) {
        authorService.update(id, request);
    }
}
