from fastapi import APIRouter
from api.routes.thumbnail import thumbnail_router
from api.routes.summary import summary_router

api_router = APIRouter()

api_router.include_router(thumbnail_router, prefix="/ai", tags=["Thumbnail"])
api_router.include_router(summary_router, prefix="/ai", tags=["Summary"])
