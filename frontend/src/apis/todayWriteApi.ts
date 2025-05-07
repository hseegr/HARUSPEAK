import { TodayWrite } from '@/types/todayWrite';

import { axiosInstance } from './core';

// 일기 작성하기 -> 데이터 보내기
export const todayWriteSend = async (data: TodayWrite) => {
  const response = await axiosInstance.post('/today', data);
  return response.data;
};

// 이미지 base64 변환 함수
export const imageToBase64 = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (typeof reader.result === 'string') {
        resolve(reader.result);
      } else {
        reject(new Error('파일 변환 실패'));
      }
    };
    reader.onerror = error => reject(error);
  });
};
