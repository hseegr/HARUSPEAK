from dotenv import load_dotenv
import os
from pydantic import BaseModel, Field
from langchain_core.output_parsers import PydanticOutputParser
from langchain_core.prompts import PromptTemplate
from langchain_openai import ChatOpenAI

# .env 파일에서 환경 변수 로드
load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")
BASE_URL = os.getenv("BASE_URL")


# 응답 스키마 정의
class KeywordOutput(BaseModel):
    title: str = Field(description="일기 제목")
    summary: str = Field(description="일기 요약")


parser = PydanticOutputParser(pydantic_object=KeywordOutput)

# 프롬프트 템플릿
prompt = PromptTemplate(
    template="""
다음은 사용자의 일기입니다. 바로 아래 지시사항을 읽고 일기의 전체 내용을 대표할 수 있게 짧고 압축적으로 제목과 요약을 작성해주세요.
1) [일기 제목]의 경우에는 다음을 따라주세요.'
    - 일기의 전체 내용이 하나의 제목으로 잘 축약될 수 있도록 작성해주세요.
    - 일기 내용 중 특별했던 일이 있다면 제목에 잘 포함되도록 해주세요.
    - 명사로 끝날 수 있도록 제목을 작성해주세요.
    예를 들어 '비가 왔고 슬펐던 날이었다.' 보다 '비가 내리던 슬펐던 날' 이런 식으로 작성해주세요.
    - - MySQL 에서 CHAR_LENGTH() 기준으로 공백, 줄바꿈, 특수문자 포함해서 글자수를 셌을 때, 제목이 25자 미만이 되도록 요약해주세요.
2) [일기 요약]의 경우에는 다음을 따라주세요.
    - 전체 내용을 하나의 글로써 요약해야합니다.
    - 요약된 내용은 최소 한 문장, 최대 세 문장으로 구성되도록 써주세요.
    - 상투적인 것들 말고 그날의 특별한 이벤트가 있다면 최대한 그것을 중심으로 요약해주세요.
    - 각 문장을 완전 짧게 요약해주세요. 엄청 짧게 해주세요. 이 부분이 너무 중요해요.
    - MySQL 에서 CHAR_LENGTH() 기준으로 공백, 줄바꿈, 특수문자 포함해서 글자수를 셌을 때, 총 요약내용이 180자 미만이 되도록 요약해주세요.
    - 요약된 문장들이 서로 유기적으로 잘 연결되게끔 흐름을 자연스럽게 해주세요.
    - 응답 결과에 줄바꿈 이스케이프 문자는 생략해주세요.

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
        model="gpt-4.1",
        temperature=0.5,
        openai_api_key=OPEN_AI_API_KEY,
        base_url=BASE_URL
    )


# 체인 만들기
async def oa_generate_daily(diary: str) -> KeywordOutput:
    _input = prompt.format_prompt(diary=diary)
    output = await llm.ainvoke(_input.to_messages())
    return parser.parse(output.content)
