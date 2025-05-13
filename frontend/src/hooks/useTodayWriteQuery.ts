import { useMutation } from '@tanstack/react-query';

import { fileToText, todayWriteSend } from '@/apis/todayWriteApi';
import { TodayWrite } from '@/types/todayWrite';

// 일기 작성 쿼리
export const useTodayWriteMutation = () => {
  return useMutation({
    mutationFn: (data: TodayWrite) => todayWriteSend(data),
  });
};

// 음성 파일 업로드 STT 쿼리
export const useFileToTextMutation = () => {
  return useMutation({
    mutationFn: (audioBlob: Blob) => fileToText(audioBlob),
  });
};
