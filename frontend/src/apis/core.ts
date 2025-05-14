import axios from 'axios';
import { toast } from 'react-toastify';

export const DOMAIN = import.meta.env.VITE_API_DOMAIN;

export const axiosInstance = axios.create({
  baseURL: DOMAIN,
  timeout: 1000,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true, // refreshToken, accessToken을 쿠키로 보낼 수 있게 설정
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
  config => {
    // 오프라인 상태 체크
    if (!navigator.onLine) {
      toast.error('인터넷 연결이 끊겼습니다. 오프라인 모드로 전환됩니다.');
      return Promise.reject(new Error('오프라인 상태입니다.'));
    }
    return config;
  },
  error => Promise.reject(error),
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    // 네트워크 에러 처리
    if (!error.response) {
      toast.error('네트워크 연결을 확인해주세요.');
      return Promise.reject(error);
    }

    // 토큰이 만료된 경우에만 토큰 갱신 시도
    if (
      error.response?.status === 401 &&
      !originalRequest._retry &&
      error.response.data?.message === 'Token expired'
    ) {
      originalRequest._retry = true;
      try {
        await axios.post(
          `${import.meta.env.VITE_API_DOMAIN}/api/auth/token/refresh`,
          {}, // POST 바디는 비워도 됨 -> 왜? 쿠키에 있는 토큰을 사용하기 때문
          {
            withCredentials: true,
          },
        );
        // accessToken 재발급, refreshToken 갱신 후 원래 요청 재시도
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        // 재발급 실패 시 → 로그아웃 처리
        localStorage.removeItem('userId');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    // 토큰이 없는 경우(로그인하지 않은 상태)는 그냥 에러 반환
    return Promise.reject(error);
  },
);
