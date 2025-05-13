// 하루일기 상세조회: (일기 1개에 해당하는 api)
import { axiosInstance } from './core';

// 하루 일기 상세 보기(오늘 제외)
export const getDiary = async (summaryId: string) => {
  const response = await axiosInstance.get(`/api/summary/${summaryId}`);
  return response.data;
};

// 요약 내용 재생성
export const regenerateContent = async (summaryId: string) => {
  const response = await axiosInstance.post(
    `/api/summary/${summaryId}/content/regenerate`,
    {},
    { timeout: 30000 },
  );
  return response.data;
};

// 요약 이미지(썸네일) 재생성
export const regenerateImage = async (summaryId: string) => {
  const response = await axiosInstance.post(
    `/api/summary/${summaryId}/image/regenerate`,
    {},
    { timeout: 30000 },
  );
  return response.data;
};

// 요약 이미지 불러오기 -> 불필요 ?
// export const getImage = async (summaryId: string) => {
//   const response = await axiosInstance.get(
//     `/api/summary/${summaryId}/image/regenerate`,
//   );
//   return response.data;
// };

// 하루일기 수정(오늘 제외)
export const editDiary = async (
  summaryId: string,
  title: string,
  content: string,
) => {
  const response = await axiosInstance.patch(`/api/summary/${summaryId}`, {
    title,
    content,
  });
  return response.data;
};
