import { createBrowserRouter } from 'react-router-dom';

import BaseLayout from './components/layout/BaseLayout';
import Home from './pages/Home';
import TodayPage from './pages/Today';
import LoginPage from './pages/login/LoginPage';
import NotFound from './pages/notFound/NotFound';
import TodayWritePage from './pages/todayWritePage';
import ImageUpload from './pages/todayWritePage/components/ImageUpload';
import VoiceToText from './pages/todayWritePage/components/VoiceToText';

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
        element: <VoiceToText />,
        handle: { title: '음성 변환하기' },
      },
      {
        path: 'todaywrite/image',
        element: <ImageUpload />,
        handle: { title: '이미지 업로드하기' },
      },
      // 예시
      // { index: true, element: <OnboardingPage /> },
      // { path: 'main', element: <MainPage /> },
      // { path: 'account/login', element: <Login /> },
    ],
  },
]);
