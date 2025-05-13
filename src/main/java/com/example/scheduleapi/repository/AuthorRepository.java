package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepository {

    private final JdbcTemplate jdbc;

    public Long save(Author author) {
        String sql = """
        INSERT INTO authors (name, email, created_at, modified_at)
        VALUES (?, ?, ?, ?)
    """;

        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            var ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, author.getName());
            ps.setString(2, author.getEmail());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(author.getCreatedAt()));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(author.getModifiedAt()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }


    public Optional<Author> findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        List<Author> result = jdbc.query(sql, this::mapRowToAuthor, id);
        return result.stream().findFirst();
    }


    private Author mapRowToAuthor(ResultSet rs, int rowNum) throws SQLException {
        return new Author(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("modified_at").toLocalDateTime()
        );
    }


    public void update(Author author) {
        String sql = """
            UPDATE authors
            SET name = ?, email = ?, modified_at = ?
            WHERE id = ?
        """;

        jdbc.update(sql,
                author.getName(),
                author.getEmail(),
                Timestamp.valueOf(author.getModifiedAt()),
                author.getId()
        );
    }
}
