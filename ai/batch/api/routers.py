from fastapi import APIRouter

from api.routes.daily import daily_router
from api.routes.thumbnail import thumbnail_router
from api.routes.thumbnail_dalle import thumbnail_dalle_router

api_router = APIRouter()

api_router.include_router(thumbnail_router, prefix="/ai", tags=["Thumbnail"])
api_router.include_router(daily_router, prefix="/ai", tags=["Daily"])
api_router.include_router(thumbnail_dalle_router, prefix="/ai", tags=["Thumbnail_DALLE"])
