package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.dto.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {

    private final JdbcTemplate jdbc;

    // 일정 저장
    public Long save(Schedule s) {
        String sql = """
                INSERT INTO schedules (todo, author_id, password, created_at, modified_at)
                VALUES (?, ?, ?, ?, ?)
        """;

        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            var ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getTodo());
            ps.setLong(2, s.getAuthorId());
            ps.setString(3, s.getPassword());
            ps.setTimestamp(4, Timestamp.valueOf(s.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(s.getModifiedAt()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    // id로 단건 조회
    public Optional<Schedule> findById(Long id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        List<Schedule> result = jdbc.query(sql, this::mapRowToSchedule, id);
        return result.stream().findFirst();
    }

    public List<ScheduleResponse> findAllWithAuthor(String authorName, String modifiedDate) {

        String sql = """
        SELECT s.id, s.todo, a.name AS author_name,
               s.created_at, s.modified_at
          FROM schedules s
          JOIN authors a ON s.author_id = a.id
         WHERE 1=1
    """;

        List<Object> params = new ArrayList<>();

        if (authorName != null && !authorName.isBlank()) {
            sql += " AND a.name = ?";
            params.add(authorName);
        }
        if (modifiedDate != null && !modifiedDate.isBlank()) {
            sql += " AND DATE(s.modified_at) = ?";
            params.add(LocalDate.parse(modifiedDate));
        }
        sql += " ORDER BY s.modified_at DESC";

        return jdbc.query(sql, params.toArray(),
                (rs, n) -> new ScheduleResponse(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("author_name"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("modified_at").toLocalDateTime()
                )
        );
    }

    // DB 결과 -> Schedule 객체 변환
    private Schedule mapRowToSchedule(ResultSet rs, int rowNum) throws SQLException {
        return new Schedule(
                rs.getLong("id"),
                rs.getString("todo"),
                rs.getLong("author_id"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("modified_at").toLocalDateTime()
        );
    }

    // 일정 수정
    public int update(Schedule s) {
        String sql = """
                UPDATE schedules
                   SET todo = ?, modified_at = ?
                 WHERE id = ?
        """;
        return jdbc.update(sql,
                s.getTodo(),
                Timestamp.valueOf(s.getModifiedAt()),
                s.getId());
    }

    // 일정 삭제
    public int deleteById(Long id) {
        return jdbc.update("DELETE FROM schedules WHERE id = ?", id);
    }
}
