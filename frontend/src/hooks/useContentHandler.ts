import { useState } from 'react';

import { useRegenerateContent } from '@/hooks/useDiaryQuery';

type AlertFunction = (
  title: string,
  message: string,
  isError?: boolean,
) => void;

export const useContentHandler = (
  summaryId: string,
  showAlert: AlertFunction,
  isEditing: boolean,
) => {
  const [isContentRegenerating, setIsContentRegenerating] = useState(false);
  const regenerateContent = useRegenerateContent();

  const handleContentReset = () => {
    // 수정 모드에서는 재생성 불가
    if (isEditing) {
      showAlert(
        '안내',
        '수정 모드에서는 재생성할 수 없습니다. 먼저 수정을 완료해주세요.',
      );
      return;
    }

    // 이미 진행 중이라면 중복 요청 방지
    if (isContentRegenerating) {
      showAlert('안내', '내용 재생성이 이미 진행 중입니다.');
      return;
    }

    setIsContentRegenerating(true);

    // mutate 호출 - 에러 처리는 try/catch로
    try {
      regenerateContent(summaryId);

      // 성공하든 실패하든 일정 시간 후 로딩 상태 해제
      setTimeout(() => {
        setIsContentRegenerating(false);
      }, 2000);
    } catch (error: any) {
      console.error('내용 재생성 오류:', error);

      // 에러 코드에 따른 처리
      if (error?.code === 'ECONNABORTED') {
        showAlert(
          '안내',
          '요청 시간이 초과되었습니다. 서버 응답을 기다리는 중입니다.',
        );
      } else if (error?.response?.data?.code) {
        showAlert(
          '안내',
          error.response.data.message || '내용 재생성 중 오류가 발생했습니다.',
          true,
        );
      } else {
        showAlert('오류', '내용 재생성 중 오류가 발생했습니다.', true);
      }

      setIsContentRegenerating(false);
    }
  };

  return {
    isContentRegenerating,
    handleContentReset,
  };
};
