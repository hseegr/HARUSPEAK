from fastapi import APIRouter, HTTPException, UploadFile, File
from utils.stt_utils import stt
# OpenAI API 호출을 위한 라우터
stt_router = APIRouter()


@stt_router.post("/stt")
async def generate_stt(file: UploadFile = File(...)):
    try:
        text = await stt(file)
        return {"text": text}
    except HTTPException as e:
        raise e
