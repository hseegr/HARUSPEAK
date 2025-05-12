import { queryClient } from '@/lib/queryClient';
import useAuthStore from '@/store/userStore';

import { DOMAIN, axiosInstance } from './core';

// 구글 로그인
// 리다이렉트 -> 브라우저가 직접 이동해야 함
export const googleLogin = async () => {
  window.location.href = `${DOMAIN}/api/auth/google/login`;
};

// 로그아웃
export const userLogout = async () => {
  try {
    queryClient.clear();
    await axiosInstance.post('/api/auth/logout');
    useAuthStore.getState().clearUser();
  } catch {
    throw new Error('로그아웃 처리 중 오류가 발생했습니다');
  }
};

// userInfo API 제거
