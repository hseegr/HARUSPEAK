import { DOMAIN, axiosInstance } from './core';

// 구글 로그인
// 리다이렉트 -> 브라우저가 직접 이동해야 함
export const googleLogin = async () => {
  window.location.href = `${DOMAIN}/api/auth/google/login`;
};

// 로그아웃
export const userLogout = async () => {
  try {
    const response = await axiosInstance.post('/api/auth/logout');
    window.location.href = '/login';
    return response.data;
  } catch {
    throw new Error('로그아웃 처리 중 오류가 발생했습니다');
  }
};

// 유저 정보 받아오기
export const userInfo = async () => {
  const response = await axiosInstance.get('/api/user/me');
  return response.data;
};
