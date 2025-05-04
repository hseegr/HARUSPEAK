import { createBrowserRouter } from 'react-router-dom';

import BaseLayout from './components/layout/BaseLayout';
import Diary from './pages/diary';
import Home from './pages/home';
import Library from './pages/library';
import LoginPage from './pages/login';
import NotFound from './pages/notFound';
import TodayPage from './pages/today';
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
      {
        path: 'library',
        element: <Library />,
        handle: { title: '내 서재' },
      },
      {
        path: 'moments',
        element: <Library />,
        handle: { title: '나의 순간 일기들' },
      },
      {
        path: 'diary/:summaryId',
        element: <Diary />,
        handle: { title: '일기 상세' },
      },
      // 예시
      // { index: true, element: <OnboardingPage /> },
      // { path: 'main', element: <MainPage /> },
      // { path: 'account/login', element: <Login /> },
    ],
  },
]);
