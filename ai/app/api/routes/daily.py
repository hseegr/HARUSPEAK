from fastapi import APIRouter, HTTPException
from utils.daily_utils import oa_generate_daily

# OpenAI API 호출을 위한 라우터
daily_router = APIRouter()


@daily_router.post("/daily")
def generate_daily(input: str):
    try:
        daily = oa_generate_daily(input)
        return daily
    except HTTPException as e:
        raise e
