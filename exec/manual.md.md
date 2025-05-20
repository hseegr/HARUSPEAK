# 📌 배포 가이드

보안 항목은 {}로 표시되어 있으므로 본인 환경에 맞게 설정 또는 발급 받은 정보를 기입하여 주세요.


---

### ✅ 대상 환경

- EC2 서버 2대 구성

    1. **EC2 (Frontend + API + AI + DB)**
        - OS: Ubuntu 22.04
        - 퍼블릭 IP: 52.79.61.124
        - 역할: 
            - Nginx
            - React frontend
            - Spring Boot API
            - FastApi AI (API 서버와 연동)
            - MySQL
            - Redis

    2. **EC2 (Batch + AI)**
        - OS: Ubuntu 24.04
        - 퍼블릭 IP: 3.38.190.195
        - 역할: 
            - Spring Batch 서브 작업
            - FastAPI AI (배치 전용)
            - MySQL
            - Redis

---

### ✅ 필수 패키지 설치

```bash
sudo apt install -y \
apt-transport-https \
ca-certificates \
curl \
software-properties-common \
git
```

---

### ✅ Docker 설치


1. Docker GPG 키 추가
    ```bash
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    ```

2. Docker 저장소 추가
    ```bash
    sudo add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    ```

3. 패키지 목록을 업데이트트
    ```bash
    sudo apt update
    ```

4. Docker 설치
    ```bash
    sudo apt install -y docker-ce
    ```

5. Docker 서비스 시작 및 부팅 시 자동 실행 설정
    ```bash
    sudo systemctl start docker  
    sudo systemctl enable docker
    ```
---

### ✅ 프로젝트 클론 및 실행 준비

1. 서버 1 (52.79.61.124)
    ```bash
    git clone https://lab.ssafy.com/s12p31a607/S12P31A607  
    cd S12P31A607
    ```

2. 서버 2 (3.38.190.195)

    git clone https://lab.ssafy.com/s12p31a607/S12P31A607  
    cd S12P31A607/backend/batch

---

### ✅ 환경 변수 설정

1. 서버 1 (52.79.61.124)
    - DOCKER COMPOSE
        - 경로 : 레포지토리 루트/docker-compose.api.yml

        - `docker-compose.yml`
        ```bash
            version: "3.8"

            services:
            mysql:
                image: mysql:8.0
                container_name: mysql
                restart: always
                healthcheck:
                test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
                interval: 10s
                timeout: 5s
                retries: 5
                environment:
                MYSQL_DATABASE: haruspeak-api
                MYSQL_USER: {USERNAME}
                MYSQL_PASSWORD: {PASSWORD}
                MYSQL_ROOT_PASSWORD: {ROOT_PASSWORD}
                command: --default-authentication-plugin=mysql_native_password
                ports:
                - "3306:3306"
                volumes:
                - mysql_data:/var/lib/mysql 

            redis:
                image: redis:7
                container_name: redis
                restart: always
                ports:
                - "6379:6379"
                volumes:
                - redis_data:/data
                - ./config/redis.conf:/usr/local/etc/redis/redis.conf
                command: [ "redis-server", "/usr/local/etc/redis/redis.conf" ]

            api:
                build:
                context: ./backend/api
                dockerfile: Dockerfile
                container_name: api-server
                ports:
                - "8080:8080"
                networks:
                - haruspeak-net

                depends_on:
                mysql:
                    condition: service_healthy
                redis:
                    condition: service_started
                fastapi:
                    condition: service_started
            
            
            fastapi:
                build:
                context: ./ai/app
                dockerfile: Dockerfile
                container_name: fastapi-server
                ports:
                - "8000:8000"
                command: ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]


            volumes:
            mysql_data:
            redis_data:
        ```
    
    - REDIS 
        - 경로 : 레포지토리 루트/config/redis.conf
        - `redis.conf`
        ```bash
            requirepass {password}

            appendonly yes
            appendfilename "appendonly.aof"

            save 60 1
            save 300 10
            save 900 100

            dir /data
        ```

    - FRONTEND
        - 프로젝트 루트 경로 : /frontend
        - `frontend/.env`
            - 경로 : /.env
            
            ```bash
                VITE_API_DOMAIN="https://haruspeak.com"
            ```

    - BACKEND-API 
        - 백엔드 API 프로젝트 루트 : 레포지토리 루트/backend/api
        
        - `backend-api/application.yml`
            - 경로 : /src/main/resources/application.yml
        
            ```bash
                server:
                    port: 8080
                    forward-headers-strategy: native
                    tomcat:
                        remote-ip-header: x-forwarded-for
                        protocol-header: x-forwarded-proto
                        tomcat:
                        max-http-post-size: 26214400 

                spring:
                    jackson:
                        time-zone: Asia/Seoul
                        date-format: yyyy-MM-dd'T'HH:mm:ss

                    # DB
                    datasource:
                        url: jdbc:mysql://mysql:3306/haruspeak-api
                        username: {username}
                        password: {password}
                        driver-class-name: com.mysql.cj.jdbc.Driver
                        hikari:
                        maximum-pool-size: 10
                        minimum-idle: 5
                        idle-timeout: 30000
                        max-lifetime: 60000

                    # JPA
                    jpa:
                        hibernate:
                        ddl-auto: none
                        show-sql: true
                        properties:
                        hibernate:
                        show_sql: false
                        format_sql: false
                    logging:
                        level:
                        org.hibernate.SQL: off
                        org.hibernate.type.descriptor.sql.BasicBinder: debug

                    #   Redis
                    data:
                        redis:
                        host: redis # 52.79.61.124
                        port: 6379
                        password: {password}

                    # OAuth2

                    security:
                        oauth2:
                        client:
                            registration:
                            google:
                                client-id: {구글_Client_Id}
                                client-secret: {구글_Client_Secret_Key}
                                scope:
                                - openid
                                - email
                                - profile
                                redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
                                authorization-grant-type: authorization_code
                                client-name: Google
                            provider:
                            google:
                                authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
                                token-uri: https://oauth2.googleapis.com/token
                                user-info-uri: https://openidconnect.googleapis.com/v1/userinfo
                                user-name-attribute: sub
                    # S3
                    cloud:
                        aws:
                        region:
                            static: ap-northeast-2
                        credentials:
                            access-key: {S3_ACCESS-KEY}
                            secret-key: {S3_Secret_Key}
                        s3:
                            bucket: haruspeak-storage


                app:
                oauth2:
                    redirect-uri: https://haruspeak.com   
                    google:
                    authorization-uri: /oauth2/authorization/google


                jwt:
                secret-key: {SECRET_KEY}
                expiration-ms:
                    access: 604800000 #7일 AccessToken
                    refresh: 2592000000 #30일 refreshToken
                token-prefix: "Bearer "


                # Springdoc OpenAPI 설정
                springdoc:
                    api-docs:
                    path: /v3/api-docs
                    swagger-ui:
                    path: /swagger-ui.html

                # fastapi
                AI_BASE_URL: http://fastapi:8000
                AI_STT_BASE_URL: {AI_STT_BASE_URL}


                # logging      
                logging:
                level:
                    com.haruspeak: INFO
            ```

    - AI-API
        - AI for API 프로젝트 루트 : /ai/app

        - `ai-api/.env`
            - 경로 : /.env

            ```bash
                OPEN_AI_API_KEY={Open_AI_API_KEY} 
                BASE_URL="https://gms.p.ssafy.io/gmsapi/api.openai.com/v1"
                KM_OPEN_AI_API_KEY={Open_AI_API_KEY_FOR_THUMBNAIL} 
            ```

2. 서버 2 (3.36.76.231)
    - DOCKER COMPOSE
        - 경로 : 레포지토리 루트/docker-compose-api.yml
        - `docker-compose.yml`
        ```bash
            version: '3.8'

            services:
            mysql:
                image: mysql:8.0
                container_name: mysql-batch
                restart: always
                healthcheck:
                test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
                interval: 10s
                timeout: 5s
                retries: 5
                environment:
                MYSQL_DATABASE: haruspeak-batch
                MYSQL_USER: {USERNAME}
                MYSQL_PASSWORD: {PASSWORD}
                MYSQL_ROOT_PASSWORD: {ROOT_PASSWORD}
                ports:
                - "3306:3306"
                volumes:
                - mysql_data:/var/lib/mysql 

            redis:
                image: redis:7
                container_name: redis-batch
                restart: always
                ports:
                - "6379:6379"
                volumes:
                - redis_data:/data
                - ./config/redis.conf:/usr/local/etc/redis/redis.conf
                command: [ "redis-server", "/usr/local/etc/redis/redis.conf" ]

                    
            batch:
                build:
                context: ./backend/batch
                dockerfile: Dockerfile
                container_name: batch-server
                ports:
                - "8080:8080"
                
                depends_on:
                mysql:
                    condition: service_healthy
                redis:
                    condition: service_started
                ai-batch:
                    condition: service_started

                    
            ai-batch:
                build:
                context: ./ai/batch
                dockerfile: Dockerfile
                container_name: ai-batch-server
                ports:
                - "8000:8000"
                command: ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]

            volumes:
            mysql_data:
            redis_data:       
        ```

    - REDIS
        - 경로 : /config/redis.conf
        - `redis.conf`
        ```bash
            requirepass {password}

            appendonly yes
            appendfilename "appendonly.aof"

            save 60 1
            save 300 10
            save 900 100

            dir /data
            dir /data
        ```
    
    - BACKEND-BATCH
        - 백엔드 배치 프로젝트 루트 : 레포지토리 루트/backend/batch

        - `backend-batch/application.yml`
            - 경로 : /src/main/resources/application.properties
            ```bash
                # Application Name
                spring.application.name=${APP_NAME:haruspeak-batch}

                spring.jackson.time-zone=${TIME_ZONE:Asia/Seoul}
                spring.jackson.date-format=${DATE_FORMAT:yyyy-MM-dd'T':mm:ss}

                # Spring Batch
                spring.batch.jdbc.initialize-schema=${BATCH_SCHEMA_INIT:never}
                spring.batch.job.enabled=${BATCH_JOB_ENABLED:false}

                # MySQL (Database configuration)
                spring.datasource.batch.jdbc-url=${BATCH_DB_URL}
                spring.datasource.batch.username=${BATCH_DB_USERNAME}
                spring.datasource.batch.password=${BATCH_DB_PASSWORD}
                spring.datasource.batch.driver-class-name=${BATCH_DB_DRIVER:com.mysql.cj.jdbc.Driver}

                spring.datasource.api.jdbc-url=${API_DB_URL}
                spring.datasource.api.username=${API_DB_USERNAME}
                spring.datasource.api.password=${API_DB_PASSWORD}
                spring.datasource.api.driver-class-name=${API_DB_DRIVER:com.mysql.cj.jdbc.Driver}

                # Hikari Configuration (Connection pool settings)
                spring.datasource.hikari.maximum-pool-size=${MAX_POOL_SIZE:10}
                spring.datasource.hikari.minimum-idle=${MIN_IDLE:5}
                spring.datasource.hikari.connection-timeout=${CONN_TIMEOUT:30000}
                spring.datasource.hikari.idle-timeout=${IDLE_TIMEOUT:600000}
                spring.datasource.hikari.max-lifetime=${MAX_LIFETIME:1800000}

                # Redis Configuration
                spring.redis.batch.host=${BATCH_REDIS_HOST}
                spring.redis.batch.port=${BATCH_REDIS_PORT:6379}
                spring.redis.batch.password=${BATCH_REDIS_PASSWORD}

                spring.redis.api.host=${API_REDIS_HOST}
                spring.redis.api.port=${API_REDIS_PORT:6379}
                spring.redis.api.password=${API_REDIS_PASSWORD}

                # S3
                spring.cloud.aws.region.static=${AWS_REGION_STATIC}
                spring.cloud.aws.credentials.access-key=${AWS_ACCESS_KEY}
                spring.cloud.aws.credentials.secret-key=${AWS_SECRET_KEY}
                spring.cloud.aws.s3.bucket=${AWS_S3_BUCKET}

                # Swagger Configuration
                springdoc.api-docs.path=${SWAGGER_API_DOCS_PATH:/v3/api-docs}
                springdoc.swagger-ui.path=${SWAGGER_UI_PATH:/swagger-ui.html}
                springdoc.swagger-ui.operations-sorter=${SWAGGER_OP_SORTER:method}
                springdoc.swagger-ui.tags-sorter=${SWAGGER_TAG_SORTER:alpha}
                springdoc.swagger-ui.display-request-duration=${SWAGGER_DISPLAY_DURATION:true}
                springdoc.packages-to-scan=${SWAGGER_SCAN_PACKAGE:com.haruspeak.batch}

                # FastAPI Base URL
                fastapi.base-url=${FASTAPI_BASE_URL:http://localhost:8000}
                fastapi.endpoint.daily-summary=${FASTAPI_ENDPOINT_DAILY_SUMMARY}
                fastapi.endpoint.generate-thumbnail=${FASTAPI_ENDPOINT_GENERATE_THUMBNAIL}

                logging.level.com.haruspeak.batch=${LOGGING_LEVEL:INFO}

                diary.default.thumbnail=${DEFAULT_THUMBNAIL}

                thumbnail.batch.chunk=${THUMBNAIL_BATCH_CHUNK:20}
                summary.batch.chunk=${SUMMARY_BATCH_CHUNK:100}
                basic.batch.chunk=${BASIC_BATCH_CHUNK:100}
            ```

        - `backend-batch/.env`
            - 경로 : /.env

            ```bash
                # MySQL settings
                BATCH_DB_URL=jdbc:mysql://mysql-batch:3306/haruspeak-batch
                BATCH_DB_USERNAME={}
                BATCH_DB_PASSWORD={}

                API_DB_URL=jdbc:mysql://52.79.61.124:3306/haruspeak-api
                API_DB_USERNAME={}
                API_DB_PASSWORD={}

                # Redis settings
                BATCH_REDIS_HOST=redis-batch
                BATCH_REDIS_PASSWORD={}

                API_REDIS_HOST=52.79.61.124
                API_REDIS_PASSWORD={}

                # S3
                AWS_REGION_STATIC=ap-northeast-2
                AWS_ACCESS_KEY={}
                AWS_SECRET_KEY={}
                AWS_S3_BUCKET={}

                # FastAPI Base URL
                FASTAPI_BASE_URL=http://ai-batch:8000
                FASTAPI_ENDPOINT_DAILY_SUMMARY=/ai/daily
                FASTAPI_ENDPOINT_GENERATE_THUMBNAIL=/ai/daily-thumbnail-dalle
                # FASTAPI_ENDPOINT_GENERATE_THUMBNAIL=/ai/daily-thumbnail

                LOGGING_LEVEL=DEBUG

                # 기본 썸네일
                DEFAULT_THUMBNAIL={}

                THUMBNAIL_BATCH_CHUNK=10
                SUMMARY_BATCH_CHUNK=200
                BASIC_BATCH_CHUNK=500
            ```
            
    - AI-API
        - AI for API 프로젝트 루트 : /ai/app

        - `ai-api/.env`
            - 경로 : /.env

            ```bash
                OPEN_AI_API_KEY={Open_AI_API_KEY} 
                BASE_URL="https://gms.p.ssafy.io/gmsapi/api.openai.com/v1"
                KM_OPEN_AI_API_KEY={Open_AI_API_KEY_FOR_THUMBNAIL} 
            ```

---



### ✅ Nginx 설치 및 설정

Nginx는 정적 파일 서빙 및 리버스 프록시 역할로 사용됩니다.  
EC2-1 서버에 직접 설치되며, React 정적 리소스 및 Spring Boot API에 대한 라우팅을 담당합니다.

1. Nginx 설치
    ```bash
        sudo apt update  
        sudo apt install -y nginx
    ```

2. EC2에 방화벽 추가
    ```bash
        sudo ufw allow 'Nginx Full'
    ```

3. Let's Encrypt SSL 인증서 발급
    - 도메인 
    ```bash
        sudo apt install certbot python3-certbot-nginx -y
        sudo certbot --nginx -d k12a607.p.ssafy.io -d haruspeak.com -d www.haruspeak.com
 E
    ```

4. Nginx 서비스 시작 및 부팅 시 자동 실행 설정

    sudo systemctl start nginx  
    sudo systemctl enable nginx

5. 설정 파일 경로
    /etc/nginx/sites-available/default

6. 설정 (리버스 프록시 + 정적 파일 서빙)

    ```nginx
        server {
            listen 443 ssl http2;
            server_name haruspeak.com www.haruspeak.com;

            ssl_certificate /etc/letsencrypt/live/k12a607.p.ssafy.io/fullchain.pem;
            ssl_certificate_key /etc/letsencrypt/live/k12a607.p.ssafy.io/privkey.pem;
            include /etc/letsencrypt/options-ssl-nginx.conf;
            ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

            location ^~ /api/auth/google/login {
            access_log /var/log/nginx/FORCE-GOOGLE-LOGIN.log;
            proxy_pass http://localhost:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
            location ^~ /api/ {
                proxy_pass http://localhost:8080;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
            }

            location ^~ /oauth2/ {
                proxy_pass http://localhost:8080;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
            }

            location ^~ /auth/ {
                proxy_pass http://localhost:8080;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
            }

            location ^~ /login/oauth2/code/ {
                proxy_pass http://localhost:8080;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
            }

            
            location ^~ /swagger-ui/ {
                proxy_pass http://localhost:8080;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
            }

            location ^~ /v3/ {
                proxy_pass http://localhost:8080;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
            }



            location / {
                root /home/ubuntu/S12P31A607/frontend/dist;
                index index.html;
                access_log /var/log/nginx/frontend.log;
            #	try_files $uri $uri/ @backend;
            try_files $uri $uri/ /index.html;
            }

        #    location @backend {
        #	access_log /var/log/nginx/backend.log;
        #        proxy_pass http://localhost:8080;
        #        proxy_set_header Host $host;
        #        proxy_set_header X-Real-IP $remote_addr;
        #        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        #        proxy_set_header X-Forwarded-Proto $scheme;
        #    }
        }
    ```

--- 

### ✅ 애플리케이션 실행
1. 서버 1 (52.79.61.124)
    ```bash
        docker compose up -f docker-compose.api.yml -d --build 
    ```

2. 서버 2 (3.38.190.195)
    ```bash
        docker compose up -f docker-compose.batch.yml -d --build 
    ```

---

# 📌 데이터베이스
---

1. 서버 1 (52.79.61.124)
    - 사용자 및 권한 설정
    - 파일 : init.sql
    ```bash
        CREATE USER '{USERNAME}'@'%' IDENTIFIED BY '{PASSWORD}';
        GRANT ALL PRIVILEGES ON . TO '{USERNAME}'@'%' WITH GRANT OPTION;
        FLUSH PRIVILEGES;
    ```
    - 스키마
    - 파일 : api-schema.sql
    ```bash
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
    ```
    - VIEW 테이블 설정
    - 파일 : api-view.sql
    ```bash
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
    ```
2. 서버 2 (3.38.190.195)
    - 사용자 및 권한 설정
    - 파일 : init.sql
    ```bash
        CREATE USER '{USERNAME}'@'%' IDENTIFIED BY '{PASSWORD}';
        GRANT ALL PRIVILEGES ON . TO '{USERNAME}'@'%' WITH GRANT OPTION;
        FLUSH PRIVILEGES;
    ```
    - 스키마
    - 파일 : batch-schema.sql
    ```bash
        CREATE TABLE IF NOT EXISTS BATCH_JOB_INSTANCE  (
            JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
            VERSION BIGINT ,
            JOB_NAME VARCHAR(100) NOT NULL,
            JOB_KEY VARCHAR(32) NOT NULL,
            constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
        ) ENGINE=InnoDB;

        CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION  (
            JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
            VERSION BIGINT  ,
            JOB_INSTANCE_ID BIGINT NOT NULL,
            CREATE_TIME DATETIME(6) NOT NULL,
            START_TIME DATETIME(6) DEFAULT NULL ,
            END_TIME DATETIME(6) DEFAULT NULL ,
            STATUS VARCHAR(10) ,
            EXIT_CODE VARCHAR(2500) ,
            EXIT_MESSAGE VARCHAR(2500) ,
            LAST_UPDATED DATETIME(6),
            constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
            references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
        ) ENGINE=InnoDB;

        CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_PARAMS  (
            JOB_EXECUTION_ID BIGINT NOT NULL ,
            PARAMETER_NAME VARCHAR(100) NOT NULL ,
            PARAMETER_TYPE VARCHAR(100) NOT NULL ,
            PARAMETER_VALUE VARCHAR(2500) ,
            IDENTIFYING CHAR(1) NOT NULL ,
            constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
            references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
        ) ENGINE=InnoDB;

        CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION  (
            STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
            VERSION BIGINT NOT NULL,
            STEP_NAME VARCHAR(100) NOT NULL,
            JOB_EXECUTION_ID BIGINT NOT NULL,
            CREATE_TIME DATETIME(6) NOT NULL,
            START_TIME DATETIME(6) DEFAULT NULL ,
            END_TIME DATETIME(6) DEFAULT NULL ,
            STATUS VARCHAR(10) ,
            COMMIT_COUNT BIGINT ,
            READ_COUNT BIGINT ,
            FILTER_COUNT BIGINT ,
            WRITE_COUNT BIGINT ,
            READ_SKIP_COUNT BIGINT ,
            WRITE_SKIP_COUNT BIGINT ,
            PROCESS_SKIP_COUNT BIGINT ,
            ROLLBACK_COUNT BIGINT ,
            EXIT_CODE VARCHAR(2500) ,
            EXIT_MESSAGE VARCHAR(2500) ,
            LAST_UPDATED DATETIME(6),
            constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
            references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
        ) ENGINE=InnoDB;

        CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_CONTEXT  (
            STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
            SHORT_CONTEXT VARCHAR(2500) NOT NULL,
            SERIALIZED_CONTEXT TEXT ,
            constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
            references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
        ) ENGINE=InnoDB;

        CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_CONTEXT  (
            JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
            SHORT_CONTEXT VARCHAR(2500) NOT NULL,
            SERIALIZED_CONTEXT TEXT ,
            constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
            references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
        ) ENGINE=InnoDB;

        CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_SEQ (
            ID BIGINT NOT NULL,
            UNIQUE_KEY CHAR(1) NOT NULL,
            constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
        ) ENGINE=InnoDB;

        INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

        CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_SEQ (
            ID BIGINT NOT NULL,
            UNIQUE_KEY CHAR(1) NOT NULL,
            constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
        ) ENGINE=InnoDB;

        INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

        CREATE TABLE IF NOT EXISTS BATCH_JOB_SEQ (
            ID BIGINT NOT NULL,
            UNIQUE_KEY CHAR(1) NOT NULL,
            constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
        ) ENGINE=InnoDB;

        INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_SEQ);        
    ```

---

# 📌 버전 정보

--- 

### ✅ 서버 1 (52.79.61.124)
---
#### 프론트엔드 (Frontend)
```bash
- Node.js: v18.20.8  
- pnpm: 10.10.0  
- React: 18.2.0  
- React DOM: 18.2.0  
- Vite: 6.3.1  
- Tailwind CSS: 3.4.17  
- Zustand: 5.0.3  
- React Hook Form: 7.56.3  
- React Router DOM: 7.5.1  
- TanStack React Query: 5.74.4  
- Lucide React: 0.503.0  
- Zod: 3.24.4
```

--- 
#### ✅ 백엔드 (Backend)

```bash
- Java: 17.0.15  
- Spring Boot: 3.4.4  
- Gradle: 8.13 (Wrapper 사용: `./gradlew`)  
- QueryDSL: 5.0.0  
- JPA: Hibernate (Spring Boot 기본 내장)  
- Springdoc OpenAPI: 2.7.0  
- AWS SDK (S3): 3.2.1  
- WebClient (Spring WebFlux): 사용
```

---
#### ✅ AI (FastAPI)
```bash
- Python: 3.10.12  
- Uvicorn: 0.34.2  
- Starlette: 0.46.2  
- FastAPI: 0.110.1 
```

---

#### ✅ 데이터 
```bash
- MySQL: 8.0 (Docker 이미지: `mysql:8.0`)  
- Redis: 7.0 (Docker 이미지: `redis:7`)
```

--- 

1. 서버 2 (3.38.190.195)
#### ✅ 백엔드 - 배치 (Backend)

```bash
- Java: 17.0.15  
- Spring Boot: 3.4.5  
- Gradle: 8.13 (설치 중 디스크 부족으로 실패)  
- Spring Batch: 사용 (`spring-boot-starter-batch`)  
- Redis: 사용 (`spring-boot-starter-data-redis`)  
- MySQL JDBC Driver: 8.0.33  
- AWS SDK (S3): 3.2.1  
- Springdoc OpenAPI: 2.7.0
```

---
#### ✅ AI - 배치 (FastAPI)
```bash
- Python: 3.12.3  
- FastAPI: 0.115.12  
- Uvicorn: 0.34.2  
- OpenAI SDK: 1.78.1  
- LangChain Core: 0.3.59  
- LangChain OpenAI: 0.3.16  
- Pydantic: 2.11.4
```

---

#### ✅ 데이터 - 배치
```bash
- MySQL: 8.0 (Docker 이미지: `mysql:8.0`)  
- Redis: 7.0 (Docker 이미지: `redis:7`)
```