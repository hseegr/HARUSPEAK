import { useState } from 'react';

import { useNavigate } from 'react-router-dom';

import { compressImage, imageToBase64 } from '@/apis/todayWriteApi';
import { useTodayWriteMutation } from '@/hooks/useTodayWriteQuery';
import { TodayWriteStore } from '@/store/todayWriteStore';

import ImageAttachButton from './components/ImageAttachButton';
import ImageInputList from './components/ImageInputList';
import TextInput from './components/TextInput';
import TextInputList from './components/TextInputList';
import TodayWriteAlertDialog from './components/TodayWriteAlertDialog';
import VoiceToTextButton from './components/VoiceToTextButton';

const TodayWritePage = () => {
  const images = TodayWriteStore(state => state.images);
  const textBlocks = TodayWriteStore(state => state.textBlocks);
  const clearAll = TodayWriteStore(state => state.clearAll);
  const [isSaving, setIsSaving] = useState(false);

  const [alertOpen, setAlertOpen] = useState(false);
  const [alertInfo, setAlertInfo] = useState({
    title: '',
    message: '',
    confirmText: '확인',
    confirmColor: 'bg-haru-green',
  });

  const { mutate: saveDiary } = useTodayWriteMutation();
  const navigate = useNavigate();

  // 음성 -> 텍스트 변환 버튼 클릭 핸들러
  const handleVoiceButtonClick = () => {
    navigate('/todaywrite/voice');
  };

  // 이미지 첨부 버튼 클릭 핸들러
  const handleImageButtonClick = () => {
    navigate('/todaywrite/image');
  };

  // 저장 버튼 클릭 핸들러
  const handleSave = async () => {
    if (images.length === 0 && textBlocks.length === 0) return;

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
            setAlertInfo({
              title: '일기 저장 완료',
              message: '오늘의 일기가 성공적으로 저장되었습니다.',
              confirmText: '확인',
              confirmColor: 'bg-haru-green',
            });
            setAlertOpen(true);
          },
          onError: error => {
            console.error('일기 저장 실패:', error);
            setIsSaving(false);
            setAlertInfo({
              title: '저장 실패',
              message: '일기 저장에 실패했습니다. 다시 시도해주세요.',
              confirmText: '닫기',
              confirmColor: 'bg-red-500',
            });
            setAlertOpen(true);
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
      {hasContent && (
        <div className='flex flex-row justify-end'>
          <div
            onClick={handleSave}
            className='flex cursor-pointer justify-end px-2 pb-2 text-sm font-semibold text-haru-green'
          >
            {isSaving ? '저장 중' : '저장'}
          </div>
          <div
            onClick={handleClear}
            className='flex cursor-pointer justify-end px-2 pb-2 text-sm font-semibold text-haru-gray-4'
          >
            초기화
          </div>
        </div>
      )}
      <div className='flex h-full flex-col overflow-y-auto px-2 py-1 pb-[180px]'>
        {/* 이미지 첨부 리스트 */}
        <ImageInputList />

        {/* 텍스트 입력 리스트 */}
        <TextInputList />

        {/* 안내 문구 */}
        {!hasContent && (
          <div className='text-center text-sm text-gray-400'>
            오늘 일기를 작성해주세요
          </div>
        )}

        {/* 하단 고정 입력 영역 */}
        <div className='fixed bottom-[70px] left-0 right-0 mx-auto w-full max-w-96 bg-white px-2 pb-8 pt-2'>
          <div className='flex flex-col gap-4'>
            {/* 버튼들 */}
            <div className='flex gap-2 px-4'>
              <ImageAttachButton onClick={handleImageButtonClick} />
              <VoiceToTextButton onClick={handleVoiceButtonClick} />
            </div>

            {/* 텍스트 입력 */}
            <div className='px-4'>
              <TextInput />
            </div>
          </div>
        </div>
      </div>
      <TodayWriteAlertDialog
        open={alertOpen}
        onOpenChange={setAlertOpen}
        title={alertInfo.title}
        message={alertInfo.message}
        confirmText={alertInfo.confirmText}
        confirmColor={alertInfo.confirmColor}
      />
    </div>
  );
};

export default TodayWritePage;
