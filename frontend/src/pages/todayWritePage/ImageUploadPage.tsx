// TypeScript는 JSX 문법을 사용하면 내부적으로 React.createElement를 호출한다고 가정함.
// 그래서 React 변수가 필요.
import React from 'react';

import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

import { TodayWriteStore } from '@/store/todayWriteStore';

import ImageFindButton from './components/ImageFindButton';
import ImageList from './components/ImageList';

//import TodayWriteAlertDialog from './components/TodayWriteAlertDialog';

const ImageUploadPage = () => {
  const navigate = useNavigate();

  // 이미지 업로드 상태관리
  const images = TodayWriteStore(state => state.images);
  const addImages = TodayWriteStore(state => state.addImages);
  const removeImages = TodayWriteStore(state => state.removeImages);
  const clearImages = TodayWriteStore(state => state.clearImages);

  // 파일 선택
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const newFiles = Array.from(e.target.files);

      // 이미지 파일만 필터링 (MIME이 없거나 image/가 아니면 제외)
      const imageFiles = newFiles.filter(
        file => file.type && file.type.startsWith('image/'),
      );

      // 이미지 외 파일이 하나라도 있으면 토스트 알림
      if (imageFiles.length < newFiles.length) {
        toast.error('이미지 파일만 첨부할 수 있어요.');
      }

      // 10장 초과 시 제한
      const total = images.length + imageFiles.length;
      if (total > 10) {
        toast.error('이미지는 10장까지 첨부할 수 있어요.');
        return;
      }

      // 이미지 파일만 추가
      imageFiles.forEach(file => addImages(file));
    }
  };

  // 파일 삭제
  const handleRemove = (index: number) => {
    removeImages(index);
  };

  // 업로드
  const handleUpload = () => {
    navigate('/todaywrite');
  };

  // 취소
  const handleCancel = () => {
    clearImages();
    navigate('/todaywrite');
  };

  return (
    <div className='flex w-full flex-col justify-center px-4 pt-6'>
      <div className='flex min-h-96 rounded-xl border bg-gray-50 text-sm text-gray-400'>
        <div className='flex w-full flex-col justify-between p-4'>
          {/* 상단 콘텐츠 (업로드 전 or 이미지 리스트) */}
          <div className='flex-1'>
            {images.length === 0 ? (
              <div className='text-center'>이미지를 업로드 하세요</div>
            ) : (
              <ImageList images={images} onRemove={handleRemove} />
            )}
          </div>

          {/* 하단 이미지 찾기 버튼 */}
          <div className='mt-4 flex justify-end'>
            <ImageFindButton
              onChange={handleFileChange}
              disabled={images.length >= 10}
            />
          </div>
        </div>
      </div>

      {/* 업로드 / 취소 버튼 */}

      <div className='mt-4 flex justify-end gap-4 text-sm'>
        <button
          className={`font-semibold ${
            images.length === 0
              ? 'cursor-not-allowed text-haru-gray-4'
              : 'text-haru-light-green hover:text-haru-green'
          }`}
          onClick={handleUpload}
          disabled={images.length === 0}
        >
          이미지 업로드
        </button>
        <button
          className='text-haru-gray-4 hover:text-haru-gray-5'
          onClick={handleCancel}
        >
          취소
        </button>
      </div>
    </div>
  );
};

export default ImageUploadPage;
