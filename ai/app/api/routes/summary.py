from fastapi import APIRouter, HTTPException
from utils.summary_utils import oa_generate_summary

# OpenAI API 호출을 위한 라우터
summary_router = APIRouter()


@summary_router.post("/daily-summary")
async def generate_summary(input: str):

    try:
        summary = await oa_generate_summary(input)
        return summary
    except HTTPException as e:
        raise e
