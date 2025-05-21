import { createBrowserRouter, redirect } from 'react-router-dom';

import ProtectedRoute from './components/ProtectedRoute';
import BaseLayout from './components/layout/BaseLayout';
import Diary from './pages/diary';
import Home from './pages/home';
import Library from './pages/library';
import LoginPage from './pages/login';
import Moments from './pages/moments';
import NotFound from './pages/notFound';
import TodayPage from './pages/today';
import TodayWritePage from './pages/todayWritePage';
import FileToTextPage from './pages/todayWritePage/FileToTextPage';
import ImageUploadPage from './pages/todayWritePage/ImageUploadPage';
import VoiceToTextPage from './pages/todayWritePage/VoiceToTextPage';

export const router = createBrowserRouter([
  {
    element: <ProtectedRoute />,
    children: [
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
          {
            path: 'todaywrite/file',
            element: <FileToTextPage />,
            handle: { title: '음성파일 변환하기' },
          },
          {
            path: 'library',
            element: <Library />,
            handle: { title: '내 서재' },
            loader: async ({ request }) => {
              const url = new URL(request.url);
              const startDate = url.searchParams.get('startDate');
              const endDate = url.searchParams.get('endDate');

              // startDate와 endDate가 모두 있는 경우에만 날짜 검증 수행
              if (startDate && endDate) {
                // 날짜 형식 검사 (YYYY-MM-DD)
                const dateRegex = /^\d{4}-\d{2}-\d{2}$/;

                if (!dateRegex.test(startDate) || !dateRegex.test(endDate)) {
                  return redirect('/404');
                }

                // 오늘 날짜 구하기
                const today = new Date();
                today.setHours(0, 0, 0, 0);

                // 날짜 유효성 검사
                const start = new Date(startDate);
                const end = new Date(endDate);
                start.setHours(0, 0, 0, 0);
                end.setHours(0, 0, 0, 0);

                // 유효하지 않은 날짜인 경우 (예: 2024-13-45)
                if (isNaN(start.getTime()) || isNaN(end.getTime())) {
                  return redirect('/404');
                }

                // 시작일이 종료일보다 늦은 경우
                if (start > end) {
                  return redirect('/404');
                }

                // 미래 날짜 체크
                if (start > today || end > today) {
                  return redirect('/404');
                }
              }
              return null;
            },
          },
          {
            path: 'moments',
            element: <Moments />,
            handle: { title: '순간 일기 모아 보기' },
            loader: async ({ request }) => {
              const url = new URL(request.url);
              const startDate = url.searchParams.get('startDate');
              const endDate = url.searchParams.get('endDate');

              // 허용된 파라미터만 있는지 확인
              const allowedParams = ['startDate', 'endDate', 'userTags'];
              const hasInvalidParams = Array.from(url.searchParams.keys()).some(
                param => !allowedParams.includes(param),
              );

              if (hasInvalidParams) {
                return redirect('/404');
              }

              // startDate와 endDate가 모두 있는 경우에만 날짜 검증 수행
              if (startDate && endDate) {
                // 날짜 형식 검사 (YYYY-MM-DD)
                const dateRegex = /^\d{4}-\d{2}-\d{2}$/;

                if (!dateRegex.test(startDate) || !dateRegex.test(endDate)) {
                  return redirect('/404');
                }

                // 오늘 날짜 구하기
                const today = new Date();
                today.setHours(0, 0, 0, 0);

                // 날짜 유효성 검사
                const start = new Date(startDate);
                const end = new Date(endDate);
                start.setHours(0, 0, 0, 0);
                end.setHours(0, 0, 0, 0);

                // 유효하지 않은 날짜인 경우 (예: 2024-13-45)
                if (isNaN(start.getTime()) || isNaN(end.getTime())) {
                  return redirect('/404');
                }

                // 시작일이 종료일보다 늦은 경우
                if (start > end) {
                  return redirect('/404');
                }

                // 미래 날짜 체크
                if (start > today || end > today) {
                  return redirect('/404');
                }
              } else {
                return redirect('/404');
              }
              return null;
            },
          },
          {
            path: 'diary/:summaryId',
            element: <Diary />,
            handle: { title: '일기 상세 보기' },
            loader: async ({ params }) => {
              if (!params.summaryId || isNaN(Number(params.summaryId))) {
                return redirect('/404');
              }
              try {
                await fetch(`/api/summary/${params.summaryId}`);
                return null;
              } catch {
                return redirect('/404');
              }
            },
          },
        ],
      },
    ],
  },
  {
    path: '/login',
    element: <LoginPage />,
    handle: { title: '로그인' },
  },
  // 최상단 404 (비로그인 상태에서 잘못된 경로 접근 시)
  {
    path: '*',
    element: <NotFound />,
    handle: { title: '페이지를 찾을 수 없습니다' },
  },
]);
