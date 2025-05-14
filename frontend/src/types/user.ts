export interface UserInfo {
  id: string;
  email: string;
  name: string;
}

export interface AuthState {
  user: UserInfo | null | undefined; // undefined: 초기 로딩 상태, null: 로그인되지 않음
  setUser: (user: UserInfo | null) => void;
  clearUser: () => void;
}
