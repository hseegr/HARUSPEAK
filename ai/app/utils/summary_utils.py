from dotenv import load_dotenv
import os
import openai

# .env 파일에서 환경 변수 로드
load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")


# openAI 로 요약을 받아오는 함수
async def oa_generate_summary(input: str) -> str:
    client = openai.OpenAI(api_key=OPEN_AI_API_KEY)

    instructions = """
"""

    result = client.responses.create(
        model="gpt-4.1",
        instructions=instructions,
        input=input
    )

    return result.output_text
