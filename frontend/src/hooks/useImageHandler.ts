import { useState } from 'react';

import { useRegenerateImage } from '@/hooks/useDiaryQuery';

type AlertFunction = (
  title: string,
  message: string,
  isError?: boolean,
) => void;

export const useImageHandler = (
  summaryId: string,
  showAlert: AlertFunction,
) => {
  const [isImageRegenerating, setIsImageRegenerating] = useState(false);
  const regenerateImage = useRegenerateImage();

  // 이미지 재생성 핸들러
  const handleImageReset = () => {
    // 이미 진행 중이라면 중복 요청 방지
    if (isImageRegenerating) {
      showAlert('안내', '이미지 재생성이 이미 진행 중입니다.');
      return;
    }

    setIsImageRegenerating(true);

    // mutate 호출 - 에러 처리는 try/catch로
    try {
      regenerateImage(summaryId);

      // 성공하든 실패하든 일정 시간 후 로딩 상태 해제
      setTimeout(() => {
        setIsImageRegenerating(false);
      }, 2000);
    } catch (error: any) {
      console.error('이미지 재생성 오류:', error);

      // 에러 코드에 따른 메시지 처리
      if (error?.code === 'ECONNABORTED') {
        showAlert(
          '안내',
          '요청 시간이 초과되었습니다. 서버 응답을 기다리는 중입니다.',
        );
      } else if (error?.response?.data?.code === 40921) {
        showAlert(
          '안내',
          '대기 중인 썸네일 재생성 요청이 이미 존재합니다.',
          true,
        );
      } else {
        showAlert('오류', '이미지 재생성 중 오류가 발생했습니다.', true);
      }

      setIsImageRegenerating(false);
    }
  };

  return {
    isImageRegenerating,
    handleImageReset,
  };
};
