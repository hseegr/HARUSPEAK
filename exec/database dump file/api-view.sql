######################################################
-- # DROP VIEW --------------------------------------
-- 1. active_daily_summary
-- 2. active_daily_moments
-- 3. user_tag_details 
-- 4. moment_tag_names 
######################################################
DROP VIEW IF EXISTS active_daily_summary;
DROP VIEW IF EXISTS active_daily_moments;
DROP VIEW IF EXISTS user_tag_details;
DROP VIEW IF EXISTS moment_tag_names ;

######################################################
-- # CREATE VIEW --------------------------------------
-- 1. active_daily_summary
-- 2. active_daily_moments
-- 3. user_tag_details 
-- 4. moment_tag_names 
######################################################
-- active_daily_summary : 삭제되지 않은 하루 요약 일기 
CREATE VIEW active_daily_summary AS
SELECT  summary_id, 
                user_id,
                write_date,
                title, 
                image_url, 
        content, 
        image_generate_count, 
        content_generate_count, 
        moment_count,
        view_count
FROM daily_summary
WHERE is_deleted = 0;


-- active_daily_moments : 삭제되지 않은 하루의 순간 일기
CREATE VIEW active_daily_moments AS
SELECT m.moment_id,
            s.user_id,
    s.summary_id,
    m.content,
    m.moment_time,
    m.image_count,
    m.tag_count,
    m.view_count,
    m.created_at
FROM daily_moments m
INNER JOIN daily_summary s ON m.summary_id= s.summary_id
WHERE s.is_deleted = 0
GROUP BY m.moment_id;


-- user_tag_details : 유저 태그 상세 정보 포함
CREATE VIEW user_tag_details AS
SELECT ut.user_tag_id,
    ut.user_id,
    ut.tag_id,
    t.name AS name,
    ut.last_used_at,
    ut.total_usage_count,
    ut.search_count,
    ut.score
FROM user_tags ut
INNER JOIN tags t ON ut.tag_id = t.tag_id;


-- user_tag_details : 순간 일기별 이름 조회
CREATE VIEW moment_tag_names AS
SELECT
        mt.moment_tag_id,
    mt.moment_id,
    mt.user_tag_id,
    ut.user_id,
    t.name 
FROM
    moment_tags mt
JOIN user_tag_details ut ON mt.user_tag_id = ut.user_tag_id
JOIN tags t ON ut.tag_id = t.tag_id;
