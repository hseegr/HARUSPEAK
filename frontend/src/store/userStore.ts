import { create } from 'zustand';

import { AuthState, UserInfo } from '@/types/user';

// 로그인 상태 관리
const useAuthStore = create<AuthState>(set => ({
  user: null,
  setUser: (user: UserInfo) => set({ user }),
  clearUser: () => set({ user: null }),
}));

export default useAuthStore;
