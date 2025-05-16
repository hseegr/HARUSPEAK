import { useMutation } from '@tanstack/react-query';
import { toast } from 'react-toastify';

import { fileToText, todayWriteSend } from '@/apis/todayWriteApi';
import { TodayWrite } from '@/types/todayWrite';

// 일기 작성 쿼리
export const useTodayWriteMutation = () => {
  return useMutation({
    mutationFn: (data: TodayWrite) => todayWriteSend(data),
    // 서버에서 내려주는 에러 메시지 -> request failed with status code 500 라서 아래 메시지로 임의 작성
    onError: (error: any) => {
      const res = error.response.data;

      // 필드 에러가 있는 경우 -> 500자 초과 오류
      if (res?.fieldErrors?.length > 0) {
        toast.error('최대 500자까지만 저장할 수 있습니다.');
      }

      // 일반 메시지가 있는 경우 -> 내용 입력 오류
      else if (res?.message) {
        toast.error('내용을 입력해주세요.');
      }

      // fallback 메시지 -> 일기 저장 실패
      else {
        toast.error('순간 기록 저장에 실패했습니다.');
      }
    },
  });
};

// 음성 파일 업로드 STT 쿼리
export const useFileToTextMutation = () => {
  return useMutation({
    mutationFn: (audioBlob: Blob) => fileToText(audioBlob),
    // 서버에서 내려주는 에러 메시지 -> request failed with status code 500 라서 아래 메시지로 임의 작성
    onError: () => {
      toast.error('텍스트 변환에 실패했어요.');
    },
  });
};
