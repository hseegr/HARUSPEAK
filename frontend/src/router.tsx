import { createBrowserRouter } from 'react-router-dom';

import BaseLayout from './components/layout/BaseLayout';
import Home from './pages/home';
import LoginPage from './pages/login';
import NotFound from './pages/notFound/index';
import TodayPage from './pages/today';
import TodayWritePage from './pages/todayWritePage';
import ImageUploadPage from './pages/todayWritePage/ImageUploadPage';
import VoiceToTextPage from './pages/todayWritePage/VoiceToTextPage';

export const router = createBrowserRouter([
  {
    path: '/login',
    element: <LoginPage />,
    handle: { title: '로그인' },
  },
  {
    element: <BaseLayout />,
    children: [
      { path: '/', element: <Home />, handle: { title: undefined } },
      {
        path: 'today',
        element: <TodayPage />,
        handle: { title: '오늘의 기록' },
      },
      {
        path: '*',
        element: <NotFound />,
        handle: { title: '페이지를 찾을 수 없습니다' },
      },
      {
        path: 'todaywrite',
        element: <TodayWritePage />,
        handle: { title: '오늘의 순간 만들기' },
      },
      {
        path: 'todaywrite/voice',
        element: <VoiceToTextPage />,
        handle: { title: '음성 변환하기' },
      },
      {
        path: 'todaywrite/image',
        element: <ImageUploadPage />,
        handle: { title: '이미지 업로드하기' },
      },
    ],
  },
]);
