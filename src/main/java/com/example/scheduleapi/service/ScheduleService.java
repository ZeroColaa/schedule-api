package com.example.scheduleapi.service;

import com.example.scheduleapi.entity.Author;
import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.dto.*;
import com.example.scheduleapi.repository.AuthorRepository;
import com.example.scheduleapi.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final AuthorRepository authorRepository;

    // 일정 생성
    public ScheduleResponse create(ScheduleCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        // 작성자 검증
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("작성자가 존재하지 않습니다."));

        Schedule schedule = new Schedule(
                null,
                request.getTodo(),
                request.getAuthorId(),
                request.getPassword(),
                now,
                now
        );

        Long id = scheduleRepository.save(schedule);

        Schedule saved = new Schedule(
                id,
                schedule.getTodo(),
                schedule.getAuthorId(),
                schedule.getPassword(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );

        return ScheduleResponse.from(saved, author.getName());
    }

    // 전체 조회
    public List<ScheduleResponse> findAll(String authorName, String modifiedDate){
        return scheduleRepository.findAllWithAuthor(authorName, modifiedDate);
    }


    // 단일 일정 조회 (id)
    public ScheduleResponse findById(Long id) {
        Schedule s = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        Author author = authorRepository.findById(s.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("작성자가 존재하지 않습니다."));

        return ScheduleResponse.from(s, author.getName());
    }

    // 일정 수정
    public ScheduleResponse update(Long id, ScheduleUpdateRequest req) {
        Schedule origin = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다."));

        if (!origin.getPassword().equals(req.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Schedule updated = new Schedule(
                origin.getId(),
                req.getTodo(),
                origin.getAuthorId(),
                origin.getPassword(),
                origin.getCreatedAt(),
                LocalDateTime.now()
        );

        scheduleRepository.update(updated);

        Author author = authorRepository.findById(updated.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("작성자가 존재하지 않습니다."));

        return ScheduleResponse.from(updated, author.getName());
    }

    // 일정 삭제
    public void delete(Long id, ScheduleDeleteRequest req) {
        Schedule s = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다."));

        if (!s.getPassword().equals(req.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.deleteById(id);
    }
}
