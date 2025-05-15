import { useState } from 'react';

import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

import { compressImage, imageToBase64 } from '@/apis/todayWriteApi';
import { useTodayWriteMutation } from '@/hooks/useTodayWriteQuery';
import { TodayWriteStore } from '@/store/todayWriteStore';

import FileToTextButton from './components/FileToTextButton';
import ImageAttachButton from './components/ImageAttachButton';
import ImageInputList from './components/ImageInputList';
import TextInput from './components/TextInput';
import TextInputList from './components/TextInputList';
import VoiceToTextButton from './components/VoiceToTextButton';

const TodayWritePage = () => {
  // 모바일 크롬 확인
  const isMobileChrome =
    /Chrome/.test(navigator.userAgent) &&
    /Mobile/.test(navigator.userAgent) &&
    /Android/.test(navigator.userAgent);

  console.log(isMobileChrome);

  const images = TodayWriteStore(state => state.images);
  const textBlocks = TodayWriteStore(state => state.textBlocks);
  const clearAll = TodayWriteStore(state => state.clearAll);
  const [isSaving, setIsSaving] = useState(false);

  const { mutate: saveDiary } = useTodayWriteMutation();
  const navigate = useNavigate();

  const isTooLong = textBlocks.join('\n\n').length > 500;

  // 음성 -> 텍스트 변환 버튼 클릭 핸들러
  const handleVoiceButtonClick = () => {
    navigate('/todaywrite/voice');
  };

  // 이미지 첨부 버튼 클릭 핸들러
  const handleImageButtonClick = () => {
    navigate('/todaywrite/image');
  };

  // 파일 -> 텍스트 변환 버튼 클릭 핸들러
  const handleFileButtonClick = () => {
    navigate('/todaywrite/file');
  };

  // 저장 버튼 클릭 핸들러
  const handleSave = async () => {
    if (images.length === 0 && textBlocks.length === 0) return;

    if (isTooLong) {
      toast.error('최대 500자까지만 저장할 수 있습니다.');
      return;
    }

    try {
      setIsSaving(true);

      // 이미지 파일을 canvas로 압축하고 base64로 변환
      const base64Images = await Promise.all(
        images.map(async file => {
          const compressed = await compressImage(file);
          return await imageToBase64(compressed);
        }),
      );

      // 텍스트 블록을 하나의 문자열로 합치기
      const content = textBlocks.join('\n\n');

      // 일기 저장 API 호출
      saveDiary(
        {
          content,
          images: base64Images,
        },
        {
          onSuccess: () => {
            // 저장 성공 후 스토어 초기화
            clearAll();
            setIsSaving(false);
            navigate('/today');
          },
          onError: error => {
            console.error('일기 저장 실패:', error);
            setIsSaving(false);
          },
        },
      );
    } catch (error) {
      console.error('일기 저장 처리 중 오류:', error);
      setIsSaving(false);
    }
  };

  // 초기화 버튼 클릭 핸들러
  const handleClear = () => {
    clearAll();
  };

  const hasContent = textBlocks.length > 0 || images.length > 0;

  return (
    <div className='relative h-full w-full'>
      <div className='flex h-full flex-col overflow-y-auto px-2 py-1 pb-[180px]'>
        {/* 이미지 첨부 리스트 */}
        <ImageInputList />

        {/* 텍스트 입력 리스트 */}
        <TextInputList />

        {/* 안내 문구 */}
        {!hasContent && (
          <div className='flex min-h-[55vh] items-center justify-center text-center text-sm text-gray-400'>
            오늘의 순간을 기록해주세요!
          </div>
        )}

        {/* 하단 고정 입력 영역 */}
        <div className='fixed bottom-[70px] left-0 right-0 mx-auto w-full max-w-96 bg-white px-2 pb-8 pt-2'>
          <div className='flex flex-col gap-4'>
            {hasContent && (
              <div className='flex flex-row justify-end px-4'>
                <div
                  onClick={handleSave}
                  className='flex cursor-pointer justify-end px-2 text-sm font-semibold text-haru-green'
                >
                  {isSaving ? '저장 중' : '저장'}
                </div>
                <div
                  onClick={handleClear}
                  className='flex cursor-pointer justify-end px-2 text-sm font-semibold text-haru-gray-4'
                >
                  초기화
                </div>
              </div>
            )}
            {/* 버튼들 */}
            <div className='flex justify-between px-4'>
              <div className='flex gap-2'>
                <ImageAttachButton onClick={handleImageButtonClick} />
                <FileToTextButton onClick={handleFileButtonClick} />
                {!isMobileChrome && (
                  <VoiceToTextButton onClick={handleVoiceButtonClick} />
                )}
              </div>
            </div>

            {/* 텍스트 입력 */}
            <div className='px-4'>
              <TextInput />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TodayWritePage;
