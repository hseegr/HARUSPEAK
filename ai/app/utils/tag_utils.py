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
    first: str = Field(description="첫 번째 태그")
    second: str = Field(description="두 번째 태그")
    third: str = Field(description="세 번째 태그")


parser = PydanticOutputParser(pydantic_object=KeywordOutput)

# 프롬프트 템플릿
prompt = PromptTemplate(
    template="""
다음은 사용자의 일기입니다. 전체 내용을 대표할 수 있는 짧고 압축적인 핵심 키워드를 추출해 주세요.
- 각 키워드는 명사형으로 5자 이내로 작성해 주세요.
- 감정, 주제, 활동 등을 대표하는 단어여야 합니다.

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
def oa_generate_tag(diary: str) -> KeywordOutput:
    _input = prompt.format_prompt(diary=diary)
    output = llm(_input.to_messages())
    return parser.parse(output.content)
