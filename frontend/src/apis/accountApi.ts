import { axiosInstance } from './core';

const DOMAIN = import.meta.env.VITE_API_DOMAIN;

// 구글 로그인
// 리다이렉트 -> 브라우저가 직접 이동해야 함
export const googleLogin = async () => {
  window.location.href = `${DOMAIN}/auth/google/login`;
};

// 로그아웃
export const userLogout = async () => {
  const response = await axiosInstance.post('/auth/logout');
  return response.data;
};

// 유저 정보 받아오기
export const userInfo = async () => {
  const response = await axiosInstance.get('/user/me');
  return response.data;
};
