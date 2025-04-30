from fastapi import APIRouter, HTTPException
from utils.tag_utils import oa_generate_tag

# OpenAI API 호출을 위한 라우터
tag_router = APIRouter()


@tag_router.post("/moment-tag")
def generate_tag(input: str):
    try:
        tag = oa_generate_tag(input)
        return tag
    except HTTPException as e:
        raise e
