from fastapi import APIRouter
from api.routes.thumbnail import thumbnail_router

api_router = APIRouter()

api_router.include_router(thumbnail_router, prefix="/ai", tags=["Thumbnail"])
