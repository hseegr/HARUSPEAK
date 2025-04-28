// vite.config.ts
import react from '@vitejs/plugin-react';
import { defineConfig } from 'vite';
import { VitePWA } from 'vite-plugin-pwa';
import tsconfigPaths from 'vite-tsconfig-paths';

export default defineConfig({
  plugins: [
    react(),
    tsconfigPaths(), // 경로 별칭 지원
    VitePWA({
      registerType: 'autoUpdate', // 자동 업데이트 설정
      // 캐싱할 정적 자산
      includeAssets: [
        'favicon.svg',
        'robots.txt',
        'apple-touch-icon.png',
        'pwa-192.png',
        'pwa-512.png',
      ],
      manifest: {
        name: '하루스픽',

        // ESLint의 naming-convention 규칙은 snake_case를 허용하지 않지만,
        // Web App Manifest 명세에서는 short_name 속성이 필수이며, snake_case로 정의되어 있습니다.
        // 따라서 ESLint 규칙을 해당 줄에서만 비활성화합니다.
        // eslint-disable-next-line @typescript-eslint/naming-convention
        short_name: '하루스픽',

        description: '하루의 peak를 기록하는 일기',

        // 마찬가지로 theme_color 속성도 명세에 따라 snake_case로 정의되어 있어,
        // ESLint 규칙을 해당 줄에서만 비활성화합니다.
        // eslint-disable-next-line @typescript-eslint/naming-convention
        theme_color: '#ffffff',
        // eslint-disable-next-line @typescript-eslint/naming-convention
        background_color: '#ffffff',
        display: 'standalone',
        orientation: 'portrait',
        // eslint-disable-next-line @typescript-eslint/naming-convention
        start_url: '/',
        icons: [
          {
            src: 'pwa-192.png',
            sizes: '192x192',
            type: 'image/png',
          },
          {
            src: 'pwa-512x512.png',
            sizes: '512x512',
            type: 'image/png',
          },
        ],
      },
      // 워크박스 설정
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg,woff,woff2,ttf,eot}'],
        cleanupOutdatedCaches: true,
      },

      // 개발 중에도 PWA 기능 테스트 가능
      devOptions: {
        enabled: true,
        type: 'module',
      },
    }),
  ],
});
