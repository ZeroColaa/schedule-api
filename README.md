#  Schedule API (Lv3)

##  프로젝트 개요

* 작성자(Author) 관리 + 일정(Schedule) 관리 API
* Author와 Schedule의 1\:N 관계 설정
* DB: MySQL + Spring JDBC

---

##  ERD (Entity Relationship Diagram)

```
authors (1) : schedules (N)

schedules.author_id → authors.id (Foreign Key)
```

![ERD](https://github.com/user-attachments/assets/cfcb825d-706a-4336-b4ab-ccbccbe37368)

---

##  API 명세서 (Lv3)

| API 명    | Method | URL                                            | Request                                               | Response                                                                               | 상태코드           |
| -------- | ------ | ---------------------------------------------- | ----------------------------------------------------- | -------------------------------------------------------------------------------------- | -------------- |
| 작성자 등록   | POST   | /authors                                       | `{ "name": "...", "email": "..." }`                   | `{ "id": 1, "name": "...", "email": "...", "createdAt": "...", "modifiedAt": "..." }`  | 201 Created    |
| 일정 등록    | POST   | /schedules                                     | `{ "todo": "...", "authorId": 1, "password": "..." }` | `{ "id": 1, "todo": "...", "author": "홍길동", "createdAt": "...", "modifiedAt": "..." }` | 201 Created    |
| 전체 일정 조회 | GET    | /schedules?author=홍길동\&modifiedDate=2025-05-10 | (QueryParam)                                          | `[ { ... }, { ... } ]`                                                                 | 200 OK         |
| 개별 일정 조회 | GET    | /schedules/{id}                                | (PathVariable)                                        | `{ "id": 1, "todo": "...", "author": "홍길동", "createdAt": "...", "modifiedAt": "..." }` | 200 OK         |
| 일정 수정    | PUT    | /schedules/{id}                                | `{ "todo": "...", "password": "..." }`                | `{ 수정된 일정 응답 }`                                                                        | 200 OK         |
| 일정 삭제    | DELETE | /schedules/{id}                                | `{ "password": "..." }`                               | -                                                                                      | 204 No Content |

---

##  실행 방법

### 1. MySQL 서버 실행



### 2. 데이터베이스 생성

```sql
CREATE DATABASE schedule_db;
USE schedule_db;
```

### 3. 테이블 생성 (schema.sql 실행)

```sql
-- authors 테이블 생성
CREATE TABLE IF NOT EXISTS authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL
);

-- schedules 테이블 생성 (author_id FK)
CREATE TABLE IF NOT EXISTS schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    todo VARCHAR(255) NOT NULL,
    author_id BIGINT NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES authors(id)
);
```

### 4. DB 연결 정보 설정 (application.yml)

`src/main/resources/application.yml` 에 다음과 같이 설정:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/schedule_db
    username: 본인계정이름
    password: 본인계정비밀번호
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 5. 프로젝트 실행

터미널에서:

```bash
./gradlew bootRun
```

또는 IntelliJ Run 버튼 클릭

### 6. Postman 등으로 API 테스트

---

##  전체 흐름 구조

```
[Client] (Postman, Frontend)
      ⇅ API 호출
[Controller] - 요청 수신/응답 반환
      ⇅
[Service] - 비즈니스 로직 처리
      ⇅
[Repository] - DB 접근 (JDBC Template)
      ⇅
[MySQL DB] - authors / schedules 테이블
```

예시: 일정 생성 흐름
`POST /schedules`
→ ScheduleController.create()
→ ScheduleService.create()
→ ScheduleRepository.save()
→ DB Insert
→ 응답 반환 (ScheduleResponse)

