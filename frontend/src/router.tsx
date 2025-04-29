import { createBrowserRouter } from 'react-router-dom';

import BaseLayout from './components/layout/BaseLayout';
import Home from './pages/Home';
import NotFound from './pages/NotFound/NotFound';
import TodayPage from './pages/Today';

export const router = createBrowserRouter([
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
      // 예시
      // { index: true, element: <OnboardingPage /> },
      // { path: 'main', element: <MainPage /> },
      // { path: 'account/login', element: <Login /> },
    ],
  },
]);
