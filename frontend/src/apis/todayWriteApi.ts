import { TodayWrite } from '@/types/todayWrite';

import { axiosInstance } from './core';

// 일기 작성하기 -> 데이터 보내기
export const todayWriteSend = async (data: TodayWrite) => {
  const response = await axiosInstance.post('/api/today', data, {
    timeout: 5000,
  });
  return response.data;
};

// 음성 파일 업로드 STT
export const fileToText = async (audioBlob: Blob) => {
  const formData = new FormData();
  formData.append('file', audioBlob, 'voice.mp3');
  const response = await axiosInstance.post(
    '/api/today/voice-to-text',
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' },
      timeout: 300000,
    },
  );
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

// canvas 이미지 변환 함수 -> 이미지 크기 압축하기
export const compressImage = (
  file: File,
  maxWidth = 600,
  quality = 0.6,
): Promise<File> => {
  return new Promise((resolve, reject) => {
    // GIF는 변환하지 않고 그대로 반환
    if (file.type === 'image/gif') {
      return resolve(file);
    }
    const reader = new FileReader();

    reader.onload = () => {
      // 파일 이미지로 읽기
      const img = new Image();
      img.src = reader.result as string;

      // canvas로 이미지 그리면서 압축
      img.onload = () => {
        const canvas = document.createElement('canvas');
        const scale = maxWidth / img.width;
        const width = maxWidth;
        const height = img.height * scale;

        // 최종 이미지 사이즈
        canvas.width = width;
        canvas.height = height;

        // canvas의 2d 컨텍스트를 가져옴
        // 2d 컨텍스트는 drawImage, fillRect 등 2차원 그래픽 작업을 위한 메서드 제공
        const ctx = canvas.getContext('2d');

        // 컨텍스트를 가져오지 못한 경우 에러 처리
        if (!ctx) return reject(new Error('Canvas context not available'));

        // 위에서 설정한 크기로 이미지 그리기
        ctx.drawImage(img, 0, 0, width, height);

        // 압축한 이미지를 blob로 변환
        // blob -> 브라우저에서 압축 + 리사이징된 이미지를 파일 형태로 서버에 전송
        canvas.toBlob(
          blob => {
            if (!blob) return reject(new Error('Blob compression failed'));
            const compressedFile = new File([blob], file.name, {
              type: 'image/jpeg',
              lastModified: Date.now(),
            });
            resolve(compressedFile);
          },
          'image/jpeg',
          quality,
        );
      };

      img.onerror = e => reject(e);
    };

    reader.onerror = e => reject(e);
    reader.readAsDataURL(file);
  });
};
