from dotenv import load_dotenv
import os
from openai import AsyncOpenAI
from fastapi import UploadFile, File
import io

load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")
BASE_URL = os.getenv("BASE_URL")


client = AsyncOpenAI(api_key=OPEN_AI_API_KEY, base_url=BASE_URL)


async def stt(file: UploadFile = File(...)) -> str:
    contents = await file.read()
    audio_file = io.BytesIO(contents)
    audio_file.name = file.filename
    result = await client.audio.transcriptions.create(
        model="whisper-1",
        file=audio_file,
    )
    return result.text
