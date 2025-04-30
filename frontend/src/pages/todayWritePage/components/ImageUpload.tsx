import { useState } from 'react';

import { useNavigate } from 'react-router-dom';

import ImageFindButton from './ImageFindButton';
import ImageList from './ImageList';

const ImageUpload = () => {
  const navigate = useNavigate();
  const [images, setImages] = useState<File[]>([]);

  // 파일 선택
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const newFiles = Array.from(e.target.files);
      setImages(prev => {
        const combined = [...prev, ...newFiles];
        if (combined.length > 10) {
          // 토스트 ???
          // alert('이미지는 최대 10개까지만 첨부할 수 있어요.');
          return prev; // 변경하지 않음
        }
        return combined;
      });
    }
  };

  // 파일 삭제
  const handleRemove = (index: number) => {
    setImages(prev => prev.filter((_, i) => i !== index));
  };

  // 업로드 (아직 콘솔 처리)
  const handleUpload = () => {
    console.log('업로드할 이미지 목록:', images);
  };

  // 취소
  const handleCancel = () => {
    setImages([]);
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
          className='font-semibold text-haru-green'
          onClick={handleUpload}
        >
          이미지 업로드
        </button>
        <button className='text-haru-gray-4' onClick={handleCancel}>
          취소
        </button>
      </div>
    </div>
  );
};

export default ImageUpload;
