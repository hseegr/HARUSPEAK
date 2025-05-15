from fastapi import APIRouter, HTTPException
from utils.daily_utils import oa_generate_daily
from pydantic import BaseModel
# OpenAI API 호출을 위한 라우터
daily_router = APIRouter()


class DailyRequest(BaseModel):
    content: str


@daily_router.post("/daily")
async def generate_daily(request: DailyRequest):
    try:
        daily = await oa_generate_daily(request)
        return daily
    except HTTPException as e:
        raise e
