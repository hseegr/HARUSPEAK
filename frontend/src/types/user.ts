export interface UserInfo {
  userId: number;
  name: string;
}

export interface AuthState {
  user: UserInfo | null;
  setUser: (user: UserInfo) => void;
  clearUser: () => void;
}
