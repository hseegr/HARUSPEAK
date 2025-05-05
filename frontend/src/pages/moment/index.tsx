import { useEffect, useState } from 'react';

import { useLocation, useParams } from 'react-router-dom';

import ImageDialog from '@/components/ImageDialog';
import { useGetMoment } from '@/hooks/useMomentQuery';
import { parseMomentTime } from '@/lib/timeUtils';
import { getMockMoment } from '@/mock/mockMomentData';

import MomentDetailCard from './components/MomentDetailCard';

// 순간 일기 상세 페이지
const Moment = () => {
  const { momentId } = useParams();
  const location = useLocation();

  const [mockData, setMockData] = useState<any>(null);
  const [isImageDialogOpen, setIsImageDialogOpen] = useState(false);
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  // 실제 API 호출
  const { data, isPending, isError } = useGetMoment(momentId || '');

  // 디버깅 로그 추가
  useEffect(() => {
    console.log('Moment 컴포넌트 마운트/업데이트');
    console.log('현재 경로:', location.pathname);
    console.log('momentId:', momentId);
  }, [location, momentId]);

  // Mock 데이터 로드 (실제 환경에서는 제거)
  useEffect(() => {
    try {
      if (momentId) {
        console.log('momentId 변경됨, Mock 데이터 로드 시도:', momentId);
        const mockMomentData = getMockMoment(momentId);
        console.log('로드된 Mock 데이터:', mockMomentData);
        setMockData(mockMomentData);
      }
    } catch (error) {
      console.error('Mock 데이터 로드 실패:', error);
    }
  }, [momentId]);

  // 실제 데이터 또는 Mock 데이터 사용
  const momentData = data || mockData;

  if (isPending && !mockData) {
    return (
      <div className='flex h-48 items-center justify-center'>
        <div className='h-8 w-8 animate-spin rounded-full border-4 border-gray-200 border-t-blue-500' />
      </div>
    );
  }

  if (isError && !mockData) {
    return (
      <div className='flex h-48 items-center justify-center text-red-500'>
        데이터를 불러오는 중 오류가 발생했습니다.
      </div>
    );
  }

  // 데이터가 없는 경우 처리
  if (!momentData) {
    return (
      <div className='flex h-48 items-center justify-center text-gray-500'>
        데이터를 찾을 수 없습니다. (ID: {momentId})
      </div>
    );
  }

  const { date } = parseMomentTime(momentData.momentTime);

  return (
    <div className='container mx-auto px-4 py-8'>
      <div className='mb-6 flex items-center'>
        <p className='text-xl font-bold'>{date}의 순간</p>
      </div>

      <div className='mx-auto max-w-2xl'>
        <MomentDetailCard
          moment={momentData}
          onImageClick={index => {
            setSelectedImageIndex(index);
            setIsImageDialogOpen(true);
          }}
        />
      </div>

      {/* 이미지 다이얼로그 */}
      <ImageDialog
        open={isImageDialogOpen}
        onOpenChange={setIsImageDialogOpen}
        images={momentData.images}
        currentIndex={selectedImageIndex}
        momentTime={momentData.momentTime}
      />
    </div>
  );
};

export default Moment;
