import { StrictMode } from 'react';

import { QueryClientProvider } from '@tanstack/react-query';
// import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { createRoot } from 'react-dom/client';

import { queryClient } from '@/lib/queryClient';

import App from './App.tsx';
import './index.css';

/**
 * TODO : @/lib/queryClient 삭제 시 추가할 코드
 * const queryClient = new QueryClient();
.*/

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
      {/* <ReactQueryDevtools /> */}
    </QueryClientProvider>
  </StrictMode>,
);
