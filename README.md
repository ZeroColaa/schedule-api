
# Schedule API (Lv3)


##  프로젝트 개요
- 작성자 관리 + 일정 관리 API (CRUD)
- 작성자(author)와 일정(schedule)의 1:N 관계 설정
- DB: MySQL + Spring JDBC 기반


---


##  ERD (Entity Relationship Diagram)

**authors (1) : schedules (N)**  
`schedules.author_id` → `authors.id` (Foreign Key)

![ERD](https://github.com/user-attachments/assets/cfcb825d-706a-4336-b4ab-ccbccbe37368)


---


## API 명세서 


| Method | URL                                            | Request                                                            | Response                                                                                             | 상태코드      |
| ------ | ---------------------------------------------- | ------------------------------------------------------------------ | ---------------------------------------------------------------------------------------------------- | --------- |
| POST   | /authors                                       | 요청 body <br> `{ "name": "...", "email": "..." }`                   | 등록 정보 <br> `{ "id": 1, "name": "...", "email": "...", "createdAt": "...", "modifiedAt": "..." }`     | 201: 정상생성 |
| POST   | /schedules                                     | 요청 body <br> `{ "todo": "...", "authorId": 1, "password": "..." }` | 등록 정보 <br> `{ "id": 1, "todo": "...", "author": "홍길동", "createdAt": "...", "modifiedAt": "..." }`    | 201: 정상생성 |
| GET    | /schedules?author=홍길동\&modifiedDate=2025-05-10 | 요청 param                                                           | 다건 응답 정보 <br> `[ { ... }, { ... } ]`                                                                 | 200: 정상조회 |
| GET    | /schedules/{id}                                | 요청 param                                                           | 단건 응답 정보 <br> `{ "id": 1, "todo": "...", "author": "홍길동", "createdAt": "...", "modifiedAt": "..." }` | 200: 정상조회 |
| PUT    | /schedules/{id}                                | 요청 body <br> `{ "todo": "...", "password": "..." }`                | 수정 정보 <br> `{ 수정된 일정 응답 }`                                                                           | 200: 정상수정 |
| DELETE | /schedules/{id}                                | 요청 param <br> `{ "password": "..." }`                              | -                                                                                                    | 204: 정상삭제 |


---


## 실행 방법

### 1. **MySQL 서버 실행**




### 2. **schema.sql 파일로 테이블 생성**

```sql
-- authors 테이블
CREATE TABLE IF NOT EXISTS authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL
);

-- schedules 테이블 (author_id FK)
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




### 3. **DB 연결 정보 설정 (`application.yml`)**

 경로 : `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/schedule_db
    username: 본인계정이름
    password: 본인계정비밀번호
    driver-class-name: com.mysql.cj.jdbc.Driver
```

* `schedule_db` → 본인 DB 이름으로 수정
* username, password → 본인 MySQL 계정 정보로 수정




### 4. **프로젝트 실행**

```bash
./gradlew bootRun
```

or
IntelliJ IDEA Run 버튼 클릭




### 5. **Postman 등으로 API 테스트**

* API 명세서 참고
* 작성자 등록 후 일정 등록 테스트 등


---



##  전체 흐름 구조 (CRUD)

````

\[Controller] ⇄ \[Service] ⇄ \[Repository] ⇄ \[MySQL DB]



예: 일정 생성 흐름  
POST /schedules
→ ScheduleController.create()
→ ScheduleService.create()
→ ScheduleRepository.save()
→ DB insert
→ ScheduleResponse 반환




