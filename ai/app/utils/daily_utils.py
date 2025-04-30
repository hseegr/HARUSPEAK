from dotenv import load_dotenv
import os
from pydantic import BaseModel, Field
from langchain_core.output_parsers import PydanticOutputParser
from langchain_core.prompts import PromptTemplate
from langchain_openai import ChatOpenAI

# .env 파일에서 환경 변수 로드
load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")


# 응답 스키마 정의
class KeywordOutput(BaseModel):
    title: str = Field(description="일기 제목")
    summary: str = Field(description="일기 요약")


parser = PydanticOutputParser(pydantic_object=KeywordOutput)

# 프롬프트 템플릿
prompt = PromptTemplate(
    template="""
[일기]
{diary}

[출력 형식]
{format_instructions}
""",
    input_variables=["diary"],
    partial_variables={
        "format_instructions": parser.get_format_instructions()
    },
)

# LangChain 모델
llm = ChatOpenAI(
        model="gpt-4",
        temperature=0.5,
        openai_api_key=OPEN_AI_API_KEY
    )


# 체인 만들기
def oa_generate_daily(diary: str) -> KeywordOutput:
    _input = prompt.format_prompt(diary=diary)
    output = llm(_input.to_messages())
    return parser.parse(output.content)
