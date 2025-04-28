import { StrictMode } from 'react';

import { createRoot } from 'react-dom/client';

// 서비스 워커 등록
// import { registerSW } from 'virtual:pwa-register';

import App from './App.tsx';
import './index.css';

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
    <App />
  </StrictMode>,
);
