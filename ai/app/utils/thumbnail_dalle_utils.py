from dotenv import load_dotenv
import os
from langchain_core.prompts import PromptTemplate
from openai import AsyncOpenAI
from PIL import Image
import base64
import io

load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")
BASE_URL = os.getenv("BASE_URL")

client = AsyncOpenAI(api_key=OPEN_AI_API_KEY,
                     base_url=BASE_URL)


prompt_template = PromptTemplate(
    input_variables=["content"],
    template="""
- 일기내용 : {content}
- 이미지의 목적은 그 날을 한 장의 이미지로만 시각화하고 싶어서입니다.
- 하루 안에 일기 내용이 여러 개라고 해서 각각의 주제를 분기로 이미지를 만들지 말고,
그 여러 개의 주제의 전체적인 느낌을 하나로 추출해서 그것을 토대로 이미지를 만들어주세요. 이 부분도 중요해요.
- 반드시 실제 사진 느낌일 필요는 없고, 만화나 예술작품, 풍경화 같은 느낌으로 이미지를 만들어주세요. 이 부분이 중요해요.
- 분위기에 따라 이미지가 밝은 느낌이 될 수도, 어두운 느낌이 될 수 있도록 해주세요.
- 일기의 구체적인 내용은 다 담을 필요 없지만, 담는 부분에 대해서만큼은 서술된 내용을 기반으로 이미지를 만들어주세요.
- 전체적인 분위기나 화자의 감정은 드러나게끔 형상화해주세요.
- 사람은 이미지에 그려넣지 말아주세요.
- 이미지에 글씨는 들어가지 않도록 해주세요.
"""
)


async def oa_generate_thumbnail_dalle(content: str) -> str:
    user_prompt = prompt_template.format(content=content)

    result = await client.images.generate(
        model="dall-e-3",
        prompt=user_prompt,
        size="1024x1024",
        response_format="b64_json"
    )

    image_bytes = base64.b64decode(result.data[0].b64_json)

    image = Image.open(io.BytesIO(image_bytes))

    image_resize = image.resize((256, 256))

    buffered = io.BytesIO()
    image_resize.save(buffered, format="PNG")

    return base64.b64encode(buffered.getvalue()).decode('utf-8')
