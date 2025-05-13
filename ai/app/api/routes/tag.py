from fastapi import APIRouter, HTTPException
from utils.tag_utils import oa_generate_tag
from typing import List
from pydantic import BaseModel  # BaseModel을 import 추가

# OpenAI API 호출을 위한 라우터
tag_router = APIRouter()


class TagRequest(BaseModel):
    tags: List[str]
    content: str


@tag_router.post("/moment-tag")
async def generate_tag(request: TagRequest):
    # 전체 request에서 필요한 데이터 꺼내 사용
    tags = request.tags
    content = request.content

    if not content.strip():
        return {"recommendTags": []}

    try:

        tag = await oa_generate_tag(
            tags,
            content
        )
        if '아무말' in tag.recommendTags and len(tag.recommendTags) > 1:
            tag.recommendTags.remove('아무말')

        return tag
    except HTTPException as e:
        raise e
