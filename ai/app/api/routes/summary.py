from fastapi import APIRouter, HTTPException
from utils.summary_utils import oa_generate_summary
from pydantic import BaseModel  # BaseModel을 import 추가

# OpenAI API 호출을 위한 라우터
summary_router = APIRouter()


class TagRequest(BaseModel):
    content: str


@summary_router.post("/daily-summary")
async def generate_summary(request: TagRequest):
    content = request.content
    try:
        summary = await oa_generate_summary(content)
        return {"content": summary}
    except HTTPException as e:
        raise e
