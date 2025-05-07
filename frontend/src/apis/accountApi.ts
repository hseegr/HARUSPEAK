import { DOMAIN, axiosInstance } from './core';

// 구글 로그인
// 리다이렉트 -> 브라우저가 직접 이동해야 함
export const googleLogin = async () => {
  window.location.href = `${DOMAIN}/api/auth/google/login`;
};

// 로그아웃
export const userLogout = async () => {
  const response = await axiosInstance.post('/api/auth/logout');
  return response.data;
};

// 유저 정보 받아오기
export const userInfo = async () => {
  const response = await axiosInstance.get('/api/user/me');
  return response.data;
};
