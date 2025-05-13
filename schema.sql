-- authors 테이블
CREATE TABLE IF NOT EXISTS authors (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    email       VARCHAR(255)  NOT NULL,
    created_at  DATETIME      NOT NULL,
    modified_at DATETIME      NOT NULL
    );

-- schedules 테이블 (author_id FK)
CREATE TABLE IF NOT EXISTS schedules (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    todo        VARCHAR(255)  NOT NULL,
    author_id   BIGINT        NOT NULL,
    password    VARCHAR(255)  NOT NULL,
    created_at  DATETIME      NOT NULL,
    modified_at DATETIME      NOT NULL,
    CONSTRAINT fk_author
    FOREIGN KEY (author_id) REFERENCES authors(id)
    );
