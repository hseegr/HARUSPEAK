# 프로젝트 외부 서비스 정보

### ✅ Google OAuth2 (소셜 로그인)

- 사용 목적: 사용자 로그인
- 사용 API: Google OAuth 2.0 Authorization Code Flow
- 필요 설정:
  - Google Cloud Console → API 및 서비스 → 사용자 인증 정보
  - OAuth 클라이언트 ID 생성
  - 승인된 리디렉션 URI: `https://haruspeak.com/login/oauth2/code/google`
- 참고 링크: https://console.cloud.google.com/apis/credentials

### ✅ AWS S3 (파일 업로드)

- 사용 목적: 사용자 이미지 저장
- 사용 서비스: AWS S3
- 필요 설정:
  - 버킷 이름: `haruspeak-storage`
  - 권한: public-read 또는 pre-signed URL 방식
  - 리전: ap-northeast-2
- 참고 링크: https://console.aws.amazon.com/s3

### ✅ AWS EC2 (서비스 배포)

- 사용 목적: 웹 서비스 배포 서버
- 사용 서비스: AWS EC2
- 인스턴스:
  - 서버 1: API + Front + AI
  - 서버 2: Batch + AI
- 리전: ap-northeast-2 (서울)

### ✅ OpenAI API

- 사용 목적: AI 요약, STT
- 사용 서비스: OpenAI GPT / Whisper API
- 필요 설정:
  - API Key 발급: https://platform.openai.com/account/api-keys
  - 사용 모델: gpt-4, whisper-1
- 참고 링크: https://platform.openai.com/docs