import { useMutation } from '@tanstack/react-query';
import { toast } from 'react-toastify';

import { fileToText, todayWriteSend } from '@/apis/todayWriteApi';
import { TodayWrite } from '@/types/todayWrite';

// 일기 작성 쿼리
export const useTodayWriteMutation = () => {
  return useMutation({
    mutationFn: (data: TodayWrite) => todayWriteSend(data),
    // 서버에서 내려주는 에러 메시지 -> request failed with status code 500 라서 아래 메시지로 임의 작성
    onError: () => {
      toast.error('일기 저장에 실패했어요.');
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
