import { useState } from 'react';

import { useParams } from 'react-router-dom';

import { regenerateContent, regenerateImage } from '@/apis/diaryApi';
import ImageSkeleton from '@/components/ImageSkeleton';
import MomentCard from '@/components/MomentCard';
import { useGetDiary } from '@/hooks/useDiaryQuery';
import CommonAlertDialog from '@/pages/todayWritePage/components/TodayWriteAlertDialog';

import ContentEditBtn from './components/ContentEditBtn';
import ContentSummary from './components/ContentSummary';
import DiaryDeleteBtn from './components/DiaryDeleteBtn';
import DiaryHeader from './components/DiaryHeader';
import ResetBtn from './components/ResetBtn';

// 경로는 실제 위치에 맞게 조정 필요

// 일기 1개 상세 보기
const Diary = () => {
  const { summaryId } = useParams();
  const { data, isPending, isError, refetch } = useGetDiary(summaryId || '');
  const [isImageRegenerating, setIsImageRegenerating] = useState(false);
  const [isContentRegenerating, setIsContentRegenerating] = useState(false);

  // 알림 다이얼로그 상태
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertInfo, setAlertInfo] = useState({
    title: '',
    message: '',
    confirmText: '확인',
    confirmColor: 'bg-haru-green',
  });

  if (!summaryId) {
    return <div>일기를 찾을 수 없습니다.</div>;
  }

  if (isPending) {
    return <div>일기를 불러오는 중입니다...</div>;
  }

  if (isError) {
    return <div>일기를 불러오는 중 오류가 발생했습니다.</div>;
  }

  if (!data) {
    return <div>일기를 찾을 수 없습니다.</div>;
  }

  // 에러 메시지 표시 함수
  const showAlert = (title: string, message: string, isError = false) => {
    setAlertInfo({
      title,
      message,
      confirmText: '확인',
      confirmColor: isError ? 'bg-red-500' : 'bg-haru-green',
    });
    setAlertOpen(true);
  };

  const handleImageReset = async () => {
    // 이미 진행 중이라면 중복 요청 방지
    if (isImageRegenerating) {
      showAlert('안내', '이미지 재생성이 이미 진행 중입니다.');
      return;
    }

    try {
      setIsImageRegenerating(true);
      await regenerateImage(summaryId);
      // 이미지 재생성 후 데이터 새로고침
      await refetch();
    } catch (error: any) {
      console.error('이미지 재생성 오류:', error);

      // 에러 코드에 따른 메시지 처리
      if (error.response?.data?.code === 40921) {
        showAlert(
          '안내',
          '대기 중인 썸네일 재생성 요청이 이미 존재합니다.',
          true,
        );
      } else {
        showAlert('오류', '이미지 재생성 중 오류가 발생했습니다.', true);
      }
    } finally {
      setIsImageRegenerating(false);
    }
  };

  const handleContentReset = async () => {
    // 이미 진행 중이라면 중복 요청 방지
    if (isContentRegenerating) {
      showAlert('안내', '내용 재생성이 이미 진행 중입니다.');
      return;
    }

    try {
      setIsContentRegenerating(true);
      await regenerateContent(summaryId);
      // 내용 재생성 후 데이터 새로고침
      await refetch();
    } catch (error: any) {
      console.error('내용 재생성 오류:', error);

      // 에러 코드에 따른 처리
      if (error.response?.data?.code) {
        showAlert(
          '안내',
          error.response.data.message || '내용 재생성 중 오류가 발생했습니다.',
          true,
        );
      } else {
        showAlert('오류', '내용 재생성 중 오류가 발생했습니다.', true);
      }
    } finally {
      setIsContentRegenerating(false);
    }
  };

  // 이미지 재생성 중일 때 또는 서버에서 이미지 생성 중이라고 알려줄 때 또는 이미지 URL이 없을 때
  const shouldShowImageSkeleton =
    isImageRegenerating ||
    data.summary.isImageGenerating ||
    !data.summary.imageUrl;

  return (
    <div className='flex flex-col gap-5'>
      {/* 상단 */}
      <div>
        <div className=''>
          <DiaryHeader
            title={data.summary.title}
            date={data.summary.diaryDate}
          />
        </div>

        <div className='flex justify-end gap-1'>
          <ContentEditBtn />
          <DiaryDeleteBtn />
        </div>

        <div className='relative'>
          {shouldShowImageSkeleton ? (
            <ImageSkeleton />
          ) : (
            <img
              className='rounded-xl'
              src={data.summary.imageUrl}
              alt='하루 요약 이미지'
            />
          )}
          <div className='absolute bottom-2 right-2'>
            <ResetBtn
              generateCount={data.summary.imageGenerateCount}
              onReset={handleImageReset}
              isDisabled={
                isImageRegenerating ||
                isPending ||
                data.summary.isImageGenerating
              }
              type='image'
            />
          </div>
        </div>
      </div>

      {/* 하루 요약 */}
      <div className='relative'>
        <ContentSummary
          summary={data.summary.content}
          isLoading={isContentRegenerating}
        />
        <div className='absolute bottom-2 right-2'>
          <ResetBtn
            generateCount={data.summary.contentGenerateCount}
            onReset={handleContentReset}
            isDisabled={isContentRegenerating || isPending}
            type='content'
          />
        </div>
      </div>

      <div className='flex flex-col gap-4'>
        {/* 하단 - 순간 목록 */}
        {data.moments &&
          data.moments.length > 0 &&
          data.moments.map(moment => (
            <MomentCard key={moment.momentId} moment={moment} isToday={false} />
          ))}
      </div>

      {/* 알림 다이얼로그 */}
      <CommonAlertDialog
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

export default Diary;
