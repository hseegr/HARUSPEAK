import axios from 'axios';

const DOMAIN = import.meta.env.VITE_API_DOMAIN;

export const axiosInstance = axios.create({
  baseURL: DOMAIN,
  timeout: 1000,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true, // refreshToken, accessToken을 쿠키로 보낼 수 있게 설정
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
  config => {
    return config;
  },
  error => Promise.reject(error),
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
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
    return Promise.reject(error);
  },
);
