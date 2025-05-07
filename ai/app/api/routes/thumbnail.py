from fastapi import APIRouter, HTTPException
from utils.thumbnail_utils import oa_generate_thumbnail
from pydantic import BaseModel  # BaseModel을 import 추가

# OpenAI API 호출을 위한 라우터
thumbnail_router = APIRouter()


class TagRequest(BaseModel):
    content: str


@thumbnail_router.post("/daily-thumbnail")
async def generate_thumnail(request: TagRequest):
    content = request.content
    try:
        thumbnail_base64 = await oa_generate_thumbnail(content)
        return {"base64": thumbnail_base64}
    except HTTPException as e:
        raise e
