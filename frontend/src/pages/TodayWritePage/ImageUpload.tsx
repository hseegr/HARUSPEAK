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
      setImages(prev => [...prev, ...newFiles]);
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
    <div className='flex min-h-screen max-w-md flex-col justify-center px-4 pt-6'>
      <div className='flex h-96 w-full items-center justify-center rounded-xl border bg-gray-50 text-sm text-gray-400'>
        <div className=''>
          {/* 업로드 전/후 구간 */}
          {images.length === 0 ? (
            <div>이미지를 업로드 하세요</div>
          ) : (
            <ImageList images={images} onRemove={handleRemove} />
          )}

          {/* 이미지 찾기 버튼 */}
          <div className='mt-4'>
            <ImageFindButton onChange={handleFileChange} />
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
