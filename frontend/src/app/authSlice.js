import { createSlice } from "@reduxjs/toolkit";

// Lấy thông tin từ localStorage khi khởi động
const getInitialState = () => {
  const storedAuth = localStorage.getItem("auth");
  if (storedAuth) {
    try {
      return JSON.parse(storedAuth);
    } catch {
      localStorage.removeItem("auth");
      return {
        user: null,
        accessToken: null,
        refreshToken: null,
        tokenType: null,
        isAuthenticated: false,
      };
    }
  }
  return {
    user: null,
    accessToken: null,
    refreshToken: null,
    tokenType: null,
    isAuthenticated: false,
  };
};

const authSlice = createSlice({
  name: "auth",
  initialState: getInitialState(),
  reducers: {
    setCredentials: (state, action) => {
      const { accessToken, refreshToken, tokenType, user } = action.payload;
      state.user = user;
      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
      state.tokenType = tokenType;
      state.isAuthenticated = true;

      // Lưu vào localStorage
      localStorage.setItem(
        "auth",
        JSON.stringify({
          user,
          accessToken,
          refreshToken,
          tokenType,
          isAuthenticated: true,
        }),
      );
    },
    logout: (state) => {
      state.user = null;
      state.accessToken = null;
      state.refreshToken = null;
      state.tokenType = null;
      state.isAuthenticated = false;

      // Xóa khỏi localStorage
      localStorage.removeItem("auth");
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;
