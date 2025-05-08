from dotenv import load_dotenv
import os
from langchain_core.prompts import PromptTemplate
from langchain_openai import ChatOpenAI


load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")


llm = ChatOpenAI(
    model="gpt-4.1",
    temperature=0.3,
    openai_api_key=OPEN_AI_API_KEY,
    base_url="https://gms.p.ssafy.io/gmsapi/api.openai.com/v1"
)


prompt = PromptTemplate(
    input_variables=["content"],
    template="""
- 전체 내용을 하나의 글로써 요약해야합니다.
- 요약된 내용은 최소 한 문장, 최대 세 문장으로 구성되도록 써주세요.
- 요약된 문장들이 서로 유기적으로 잘 연결되게끔 흐름을 자연스럽게 해주세요.

[요약 대상]
{content}
"""
)


async def oa_generate_summary(content: str) -> str:

    _input = prompt.format(content=content)

    output = await llm.ainvoke(_input)

    return output.content.strip()
