// TypeScript는 JSX 문법을 사용하면 내부적으로 React.createElement를 호출한다고 가정함.
// 그래서 React 변수가 필요.
import React from 'react';

import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

import { TodayWriteStore } from '@/store/todayWriteStore';

import ImageFindButton from './components/ImageFindButton';
import ImageInputList from './components/ImageInputList';

// import ImageList from './components/ImageList';

//import TodayWriteAlertDialog from './components/TodayWriteAlertDialog';

const ImageUploadPage = () => {
  const navigate = useNavigate();

  // 이미지 업로드 상태관리
  const images = TodayWriteStore(state => state.images);
  const addImages = TodayWriteStore(state => state.addImages);
  // const removeImages = TodayWriteStore(state => state.removeImages);
  const clearImages = TodayWriteStore(state => state.clearImages);

  // 이미지 파일 확인 -> 헤더 바이트 확인
  const isImageByMagicNumber = async (file: File): Promise<boolean> => {
    const headerSize = 12; // 가장 긴 시그니처 길이 고려 (WEBP)
    const buffer = await file.slice(0, headerSize).arrayBuffer();
    const bytes = new Uint8Array(buffer);

    // JPEG: FF D8 FF
    if (bytes[0] === 0xff && bytes[1] === 0xd8 && bytes[2] === 0xff) {
      return true;
    }

    // PNG: 89 50 4E 47
    if (
      bytes[0] === 0x89 &&
      bytes[1] === 0x50 &&
      bytes[2] === 0x4e &&
      bytes[3] === 0x47
    ) {
      return true;
    }

    // GIF: 47 49 46 38 (GIF8)
    if (
      bytes[0] === 0x47 &&
      bytes[1] === 0x49 &&
      bytes[2] === 0x46 &&
      bytes[3] === 0x38
    ) {
      return true;
    }

    // WEBP: "RIFF....WEBP"
    const ascii = String.fromCharCode(...bytes);
    if (ascii.startsWith('RIFF') && ascii.includes('WEBP')) {
      return true;
    }

    // SVG (텍스트 기반)
    const text = await file.slice(0, 100).text();
    if (text.includes('<svg')) return true;

    return false;
  };

  // 이미지 파일 필터링
  const filterImagesByMagicNumber = async (files: File[]) => {
    const result: File[] = [];
    for (const file of files) {
      if (await isImageByMagicNumber(file)) {
        result.push(file);
      }
    }
    return result;
  };

  // 파일 선택
  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      // 선택된 파일 배열
      const newFiles = Array.from(e.target.files);

      // 이미지 파일 필터링 한 배열
      const imageFiles = await filterImagesByMagicNumber(newFiles);

      // newFiles 길이가 imageFiles 보다 크면 이미지 파일이 아닌 파일이 있는 것
      if (imageFiles.length < newFiles.length) {
        toast.error('이미지 파일만 첨부할 수 있어요.');
      }

      // 현재 업로드된 이미지들의 파일 이름을 기준으로 중복 체크
      const existingNames = new Set(images.map(file => file.name));

      // 중복된 파일 제거
      const uniqueNewFiles = imageFiles.filter(
        file => !existingNames.has(file.name),
      );

      if (uniqueNewFiles.length < imageFiles.length) {
        toast.info('중복된 이미지는 제외되었어요.');
      }

      // 남은 업로드 가능 수 계산
      const availableSlots = 10 - images.length;

      if (availableSlots <= 0) {
        toast.error('이미지는 최대 10장까지만 첨부할 수 있어요.');
        return;
      }

      // 업로드 가능 개수만큼 자르기
      const filesToAdd = uniqueNewFiles.slice(0, availableSlots);

      if (filesToAdd.length < uniqueNewFiles.length) {
        toast.info(
          `${filesToAdd.length}장만 업로드되었어요. 최대 10장까지 가능해요.`,
        );
      }

      // 상태에 추가
      filesToAdd.forEach(file => addImages(file));
    }
  };

  // 파일 삭제
  // const handleRemove = (index: number) => {
  //   removeImages(index);
  // };

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
              // <ImageList images={images} onRemove={handleRemove} />
              <ImageInputList />
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
