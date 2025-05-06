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
    recommendTags: list[str] = Field(description="추천 키워드 리스트")


parser = PydanticOutputParser(pydantic_object=KeywordOutput)

# 프롬프트 템플릿
prompt = PromptTemplate(
    input_variables=["content", "guide"],
    partial_variables={
        "format_instructions": parser.get_format_instructions()
    },
    template="""
다음은 사용자의 일기입니다. 전체 내용을 대표할 수 있는 짧고 압축적인 핵심 키워드를 추출해 주세요.
- 각 키워드는 명사형으로 5자 이내로 작성해 주세요.
- 각 키워드에 띄어쓰기는 생략해주세요.
- 예를 들어 추천 키워드가 만약 "바쁜 일상", "프로젝트 회의" 라면 "바쁜일상", "프로젝트회의" 로 해주되,
"프로젝트회의"는 6글자니까 다른 키워드를 추천해주세요.
- 감정, 주제, 활동 등을 대표하는 단어여야 합니다.
{guide}

[일기]
{content}

[출력 형식]
{format_instructions}
"""
)

# LangChain 모델
llm = ChatOpenAI(
        model="gpt-4",
        temperature=0.5,
        openai_api_key=OPEN_AI_API_KEY
)


# 체인 만들기
def oa_generate_tag(
    tags: list[str],
    content: str
) -> KeywordOutput:

    tag_len = len(tags)

    if tag_len >= 3:
        return KeywordOutput(recommendTags="")

    count = 3 - tag_len
    guide_text = f"- 총 {count}개의 키워드를 추출해 주세요."

    _input = prompt.format(
        content=content,
        guide=guide_text
    )
    output = llm.invoke(_input)

    return parser.parse(output.content)
