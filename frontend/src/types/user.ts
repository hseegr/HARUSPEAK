export interface UserInfo {
  userId: number;
  name: string;
}

export interface AuthState {
  user: UserInfo | null;
  isAuthenticated: boolean;
  setUser: (user: UserInfo) => void;
  clearUser: () => void;
}
