import { StrictMode } from 'react';

import { QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { createRoot } from 'react-dom/client';

import { queryClient } from '@/lib/queryClient';

import App from './App.tsx';
import './index.css';

// 서비스 워커 등록
// import { registerSW } from 'virtual:pwa-register';

// registerSW({
//   onNeedRefresh: () => {
//     console.log('새 버전이 있습니다. 새로고침 권장');
//   },
//   onOfflineReady: () => {
//     console.log('오프라인에서도 사용할 수 있습니다.');
//   },
// });

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
      <ReactQueryDevtools />
    </QueryClientProvider>
  </StrictMode>,
);
