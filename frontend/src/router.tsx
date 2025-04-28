import { createBrowserRouter } from 'react-router-dom';

import BaseLayout from './components/layout/BaseLayout';
import NotFound from './pages/NotFound/NotFound';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <BaseLayout />,
    children: [
      // 예시
      // { index: true, element: <OnboardingPage /> },
      // { path: 'main', element: <MainPage /> },
      // { path: 'account/login', element: <Login /> },
    ],
  },

  // 404 NotFound: 최상위에 둬야 함!
  {
    path: '*',
    element: <NotFound />,
  },
]);
