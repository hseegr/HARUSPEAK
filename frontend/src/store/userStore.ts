import { create } from 'zustand';

import { AuthState, UserInfo } from '@/types/user';

// 로그인 상태 관리
const useAuthStore = create<AuthState>(set => ({
  user: null,
  isAuthenticated: false,
  setUser: (user: UserInfo) => set({ user, isAuthenticated: true }),
  clearUser: () => set({ user: null, isAuthenticated: false }),
}));

export default useAuthStore;
