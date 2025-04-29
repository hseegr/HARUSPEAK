from dotenv import load_dotenv
import os
import openai

# .env 파일에서 환경 변수 로드
load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")


# openAI 로 썸네일을 받아오는 함수
async def oa_generate_thumbnail(input: str) -> str:
    client = openai.OpenAI(api_key=OPEN_AI_API_KEY)

    user_prompt = f"""
- 일기내용 : {input}
"""

    result = client.images.generate(
        model="gpt-image-1",
        prompt=user_prompt,
        size="1024x1024",
        quality="low",
    )

    return result.data[0].b64_json
