from fastapi import APIRouter, HTTPException
from utils.thumbnail_utils import oa_generate_thumbnail

# OpenAI API 호출을 위한 라우터
thumbnail_router = APIRouter()


@thumbnail_router.post("/daily-thumbnail")
async def generate_thumnail(input: str):

    try:
        thumbnail_base64 = await oa_generate_thumbnail(input)
        return thumbnail_base64
    except HTTPException as e:
        raise e
