from dotenv import load_dotenv
import os
from openai import AsyncOpenAI
from fastapi import UploadFile, File
from faster_whisper import WhisperModel, BatchedInferencePipeline
import io

load_dotenv()
OPEN_AI_API_KEY = os.getenv("OPEN_AI_API_KEY")
BASE_URL = os.getenv("BASE_URL")


client = AsyncOpenAI(api_key=OPEN_AI_API_KEY, base_url=BASE_URL)

model = WhisperModel("large", device="cuda", compute_type="int8")
batched_model = BatchedInferencePipeline(model=model)


async def stt(file: UploadFile = File(...)) -> str:
    contents = await file.read()
    audio_file = io.BytesIO(contents)
    audio_file.name = file.filename
    result = await client.audio.transcriptions.create(
        model="whisper-1",
        file=audio_file,
    )
    return result.text


async def stt_fasterwhisper(file: UploadFile = File(...)) -> str:
    contents = await file.read()
    audio_file = io.BytesIO(contents)
    segments, _ = batched_model.transcribe(
        audio_file, batch_size=16, vad_filter=True, initial_prompt="Please print it out in the same language"
    )
    return " ".join([segment.text for segment in segments])
