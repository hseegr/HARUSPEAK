export interface UserInfo {
  id: string;
  email: string;
  name: string;
}

export interface AuthState {
  user: UserInfo | null;
  setUser: (user: UserInfo | null) => void;
  clearUser: () => void;
}
