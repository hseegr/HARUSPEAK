from fastapi import APIRouter, HTTPException
from utils.thumbnail_dalle_utils import oa_generate_thumbnail_dalle
from pydantic import BaseModel  # BaseModel을 import 추가

# OpenAI API 호출을 위한 라우터
thumbnail_dalle_router = APIRouter()


class ThumbnailRequest(BaseModel):
    content: str


@thumbnail_dalle_router.post("/daily-thumbnail-dalle")
async def generate_thumnail(request: ThumbnailRequest):
    content = request.content
    try:
        thumbnail_base64 = await oa_generate_thumbnail_dalle(content)
        return {"base64": thumbnail_base64}
    except HTTPException as e:
        raise e
