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
- 일기 내용의 의미를 해석할 수 없는 컨텐츠의 경우 "아무말"을 추천해주세요. 그러나 "안녕하세요> ㅎㅎj" 처럼 대략적으로라도 해석할 수 있으면 태그 추천해주세요.
- 의미를 해석할 수 있는 부분이 10글자 이하면 1개의 추천태그만, 10글자에서 20글자 사이면 2개의 추천태그만, 20글자 이상이면 그때부터는 3개의 태그를 추천해주세요.  
- "아무말"이 추천태그로 제공될 경우에 그 하나만 추천해주세요. 즉 배열의 길이가 1이 되도록 해주세요.
- 일기, 즉 컨텐츠 내용이 없을 때는 빈배열을 추천해주세요.
{guide}

[일기]
{content}

[출력 형식]
{format_instructions}
"""
)

# LangChain 모델
llm = ChatOpenAI(
        model="gpt-4.1",
        temperature=0.5,
        base_url=BASE_URL,
        openai_api_key=OPEN_AI_API_KEY
)


# 체인 만들기
async def oa_generate_tag(
    tags: list[str],
    content: str
) -> KeywordOutput:

    tag_len = len(tags)

    if tag_len >= 3:
        return KeywordOutput(recommendTags=[])

    count = 3 - tag_len
    guide_text = f"- 총 {count}개의 키워드를 추출해 주세요."

    _input = prompt.format(
        content=content,
        guide=guide_text
    )
    output = await llm.ainvoke(_input)

    return parser.parse(output.content)
