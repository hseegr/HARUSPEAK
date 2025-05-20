######################################################
-- # DROP TABLE --------------------------------------
-- 1. moment_tags
-- 2. user_tags
-- 3. tags
-- 4. moment_images
-- 5. daily_moments
-- 6. daily_summary
-- 7. users
######################################################
-- enity 에서 생성된 table
DROP TABLE IF EXISTS active_daily_summary;
DROP TABLE IF EXISTS active_daily_moments;
DROP TABLE IF EXISTS user_tag_details;
DROP TABLE IF EXISTS moment_tag_names ;

DROP TABLE IF EXISTS moment_tags;
DROP TABLE IF EXISTS user_tags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS moment_images;
DROP TABLE IF EXISTS daily_moments;
DROP TABLE IF EXISTS daily_summary;
DROP TABLE IF EXISTS users;


######################################################
-- # CREATE TABLE ------------------------------------
-- 1. users
-- 2. daily_summary
-- 3. daily_moments
-- 4. moment_images
-- 5. tags
-- 6. user_tags
-- 7. moment_tags
######################################################
-- users : 사용자(회원) 
CREATE TABLE users (
    user_id	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT, -- 사용자 id PK
    sns_type	VARCHAR(10)	NOT NULL DEFAULT 'google', -- sns 로그인 종류 
    sns_id	VARCHAR(50)	NOT NULL UNIQUE, -- sns에서 제공하는 unique id
    email	VARCHAR(50)	NOT NULL UNIQUE, -- sns 제공 이메일
    name	VARCHAR(20)	NOT NULL, -- sns 제공 이름
    is_deleted	TINYINT	NOT NULL DEFAULT 0 -- 사용자 탈퇴 여부(0:false, 1:true) 
);

-- daily_summary : 하루 일기(날짜별 요약) 
CREATE TABLE daily_summary (
    summary_id	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT, -- 하루 일기 id PK
    user_id	INT	NOT NULL, -- 사용자 id FK users(user_id)
    write_date	DATE	NOT NULL, -- 날짜 
    title	VARCHAR(30)	NOT NULL, -- 하루 제목
    image_url	VARCHAR(255), -- 요약 이미지 주소
    content	VARCHAR(200)	NOT NULL, -- 요약 내용
    image_generate_count	INT	NOT NULL DEFAULT 1, -- 요약 이미지 생성 횟수
    content_generate_count	INT	NOT NULL DEFAULT 1, -- 요약 내용 생성 횟수
    moment_count	INT	NOT NULL DEFAULT 1, -- 작성한 하위 순간 일기 개수 
    view_count INT NOT NULL DEFAULT 0, -- 상세 보기 클릭 횟수 : 하루로깅
    updated_at	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 최종 수정 시각
    is_deleted	TINYINT	NOT NULL DEFAULT 0, -- 일기 삭제 여부(0:false, 1:true)
UNIQUE KEY uniq_user_date (user_id, write_date) -- 복합 UNIQUE 사용자마다 태크 중복 방지
);

-- daily_moments : 하루안의 순간 일기
CREATE TABLE daily_moments (
    moment_id	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT, -- 순간 일기 id PK
    summary_id	INT	NOT NULL, -- 하루 일기 id FK daily_summary(summary_id)
    content	VARCHAR(50)	NULL, -- 내용
    moment_time	TIMESTAMP	NOT NULL, -- 작성 시각
    image_count	INT	NOT NULL	DEFAULT 0, -- 
    tag_count	INT	NOT NULL	DEFAULT 0,
    view_count	INT	NOT NULL DEFAULT 0, -- 순간 일기 더보기 횟수 (검색 결과에서 일기 보기로) : 하루로깅
    created_at	TIMESTAMP	NOT NULL, -- 생성 시각(서버) 
    updated_at	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 최종 수정 시각
UNIQUE KEY uniq_diary_time (summary_id, moment_time) -- 복합 UNIQUE 하루에 같은 시간 중복 방지
);

-- moment_images : 순간 일기에 첨부된 이미지들
CREATE TABLE moment_images (
    image_id	INT NOT NULL PRIMARY KEY AUTO_INCREMENT, -- 이미지 id PK
    moment_id	INT	NOT NULL, -- 순간 일기 id FK daily_moments(moment_id)
    image_url	VARCHAR(255) UNIQUE NOT NULL -- S3에 저장된 이미지 주소 
);

-- tags : 태그 목록
CREATE TABLE tags (
    tag_id	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT, -- 태그 id PK,
    name	VARCHAR(5)	UNIQUE NOT NULL -- 태그 이름
);

-- user_tags : 각 사용자 태그 
-- 최근 한달 사용/검색 횟수(monthly_usage_count, monthly_serach_count) : 미사용 -> 추후 고려
CREATE TABLE user_tags (
    user_tag_id	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT, -- 사용자 태그 id PK,
    user_id	INT	NOT NULL, -- 사용자 id FK user(user_id)
    tag_id	INT	NOT NULL, -- 태그 id FK tags(tag_id)
    last_used_at	TIMESTAMP	NOT NULL, -- 최근 사용된 일자 형태 2025-04-25 00:00:00 정각
    total_usage_count	INT	NOT NULL	DEFAULT 0, -- 총 사용 횟수
    search_count	INT	NOT NULL DEFAULT 1, -- 검색 횟수
    score	INT	NOT NULL DEFAULT 0, -- 태그 사용 점수
UNIQUE KEY uniq_user_tag (user_id, tag_id) -- 복합 UNIQUE 사용자마다 태크 중복 방지
);

-- moment_tags : 일기 태그
CREATE TABLE moment_tags (
    moment_tag_id	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT, -- 순간 일기 태그 id PK,
    moment_id	INT	NOT NULL, -- 순간 일기 id FK daily_momnets(moment_id)
    user_tag_id	INT	NOT NULL, -- 태그 id FK user_tags(user_tag_id)
UNIQUE KEY uniq_moment_user_tag (moment_id, user_tag_id) -- 복합 UNIQUE 순간 일기마다 태크 중복 방지
);


######################################################
-- # ALTER : FK --------------------------------------
-- 1. daily_summary
-- 2. daily_moments
-- 3. moment_images
-- 4. user_tags
-- 5. moment_tags
######################################################
-- daily_summary
ALTER TABLE daily_summary
ADD CONSTRAINT fk_daily_summary_to_users
FOREIGN KEY (user_id)
REFERENCES users(user_id)
;

-- daily_moments
ALTER TABLE daily_moments
ADD CONSTRAINT fk_daily_moments_to_daily_summary
FOREIGN KEY (summary_id)
REFERENCES daily_summary(summary_id)
;

-- moment_images
ALTER TABLE moment_images
ADD CONSTRAINT fk_moment_images_to_daily_moments
FOREIGN KEY (moment_id)
REFERENCES daily_moments(moment_id)
;

-- user_tags
ALTER TABLE user_tags
ADD CONSTRAINT fk_user_tags_to_tags
FOREIGN KEY (tag_id)
REFERENCES tags(tag_id)
;
ALTER TABLE user_tags
ADD CONSTRAINT fk_user_tags_to_users
FOREIGN KEY (user_id)
REFERENCES users(user_id)
;

-- moment_tags
ALTER TABLE moment_tags
ADD CONSTRAINT fk_moment_tags_to_user_tags
FOREIGN KEY (user_tag_id)
REFERENCES user_tags(user_tag_id)
;


######################################################
-- # ALTER : INDEX ----------------------------------
######################################################
ALTER TABLE daily_summary ADD INDEX idx_user_deleted (user_id, is_deleted);
ALTER TABLE daily_summary ADD INDEX idx_user_date_deleted (user_id, write_date, is_deleted);
ALTER TABLE daily_moments ADD INDEX idx_diary_time (summary_id, moment_time);
