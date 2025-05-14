import { create } from 'zustand';

import { AuthState, UserInfo } from '@/types/user';

// 로그인 상태 관리
const useAuthStore = create<AuthState>(set => ({
  user: undefined, // undefined: 초기 로딩 상태, null: 로그인되지 않음
  setUser: (user: UserInfo | null) => set({ user }),
  clearUser: () => set({ user: null }),
}));

export default useAuthStore;
