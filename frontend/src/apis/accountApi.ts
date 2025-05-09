import { QueryClient } from '@tanstack/react-query';

import useAuthStore from '@/store/userStore';

import { DOMAIN, axiosInstance } from './core';

const queryClient = new QueryClient();
// 구글 로그인
// 리다이렉트 -> 브라우저가 직접 이동해야 함
export const googleLogin = async () => {
  window.location.href = `${DOMAIN}/api/auth/google/login`;
};

// 로그아웃
export const userLogout = async () => {
  try {
    await axiosInstance.post('/api/auth/logout');
    useAuthStore.getState().clearUser();
    queryClient.clear();
  } catch {
    throw new Error('로그아웃 처리 중 오류가 발생했습니다');
  }
};

// 유저 정보 받아오기
export const userInfo = async () => {
  const response = await axiosInstance.get('/api/user/me');
  return response.data;
};
