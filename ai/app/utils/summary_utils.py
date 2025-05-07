from dotenv import load_dotenv
import os
import openai

# .env 파일에서 환경 변수 로드
load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")

client = openai.OpenAI(api_key=OPEN_AI_API_KEY)


# openAI 로 요약을 받아오는 함수
async def oa_generate_summary(content: str) -> str:

    instructions = """
- 전체 내용을 하나의 글로써 요약해야합니다.
- 요약된 내용은 최소 한 문장, 최대 세 문장으로 구성되도록 써주세요.
- 요약된 문장들이 서로 유기적으로 잘 연결되게끔 흐름을 자연스럽게 해주세요.
"""

    result = client.chat.completions.create(
        model="gpt-4.1",
        messages=[
            {"role": "system", "content": instructions},
            {"role": "user", "content": content}
        ]
    )

    return result.choices[0].message.content.strip()
