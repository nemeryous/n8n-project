import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { setCredentials, logout } from "./authSlice";

const BASE_URL = import.meta.env.VITE_BASE_URL;

// Base query với retry logic để tự động refresh token
const baseQuery = fetchBaseQuery({
  baseUrl: BASE_URL,
  prepareHeaders: (headers, { getState }) => {
    // Lấy token từ state
    const token = getState().auth?.accessToken;
    if (token) {
      headers.set("authorization", `Bearer ${token}`);
    }
    headers.set("ngrok-skip-browser-warning", "true");
    return headers;
  },
});

// Wrapper để xử lý refresh token tự động
const baseQueryWithReauth = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  // Bỏ qua refresh cho các endpoint auth (login, register, refresh, logout, logout-all) để tránh vòng lặp
  const url = typeof args === "string" ? args : args?.url || "";
  const isAuthEndpoint =
    url.includes("/auth/login") ||
    url.includes("/auth/register") ||
    url.includes("/auth/refresh") ||
    url.includes("/auth/logout") ||
    url.includes("/auth/logout-all");

  // Nếu nhận 401 và không phải auth endpoint, thử refresh
  if (
    result?.error?.status === 401 &&
    !isAuthEndpoint &&
    !extraOptions?.skipRefresh
  ) {
    const state = api.getState();
    const refreshToken = state.auth?.refreshToken;

    if (refreshToken) {
      // Tạo base query riêng cho refresh để không bị ảnh hưởng bởi prepareHeaders
      const refreshBaseQuery = fetchBaseQuery({
        baseUrl: BASE_URL,
        prepareHeaders: (headers) => {
          headers.set("authorization", `Bearer ${refreshToken}`);
          headers.set("ngrok-skip-browser-warning", "true");
          return headers;
        },
      });

      // Thử refresh token
      const refreshResult = await refreshBaseQuery(
        {
          url: "auth/refresh",
          method: "POST",
        },
        api,
        { ...extraOptions, skipRefresh: true }, // Skip refresh cho chính request refresh
      );

      if (refreshResult?.data) {
        // Transform response nếu cần (tương tự như login)
        let transformedData = refreshResult.data;
        if (refreshResult.data.data) {
          // Có wrapper data
          transformedData = {
            accessToken:
              refreshResult.data.data.access_token ||
              refreshResult.data.data.accessToken,
            refreshToken:
              refreshResult.data.data.refresh_token ||
              refreshResult.data.data.refreshToken,
            tokenType:
              refreshResult.data.data.token_type ||
              refreshResult.data.data.tokenType,
            user: refreshResult.data.data.user,
          };
        } else if (refreshResult.data.access_token) {
          // Không có wrapper nhưng có snake_case
          transformedData = {
            accessToken: refreshResult.data.access_token,
            refreshToken: refreshResult.data.refresh_token,
            tokenType: refreshResult.data.token_type,
            user: refreshResult.data.user,
          };
        }
        // Lưu token mới
        api.dispatch(setCredentials(transformedData));
        // Retry request ban đầu với token mới
        result = await baseQuery(args, api, extraOptions);
      } else {
        // Refresh thất bại, đăng xuất
        api.dispatch(logout());
      }
    } else {
      // Không có refreshToken, đăng xuất
      api.dispatch(logout());
    }
  }

  return result;
};

export const authApi = createApi({
  reducerPath: "authApi",
  baseQuery: baseQueryWithReauth,
  tagTypes: ["Auth"],
  endpoints: (builder) => ({
    login: builder.mutation({
      query: (credentials) => ({
        url: "auth/login",
        method: "POST",
        body: credentials,
      }),
      transformResponse: (response) => {
        // API trả về { success, message, data: { access_token, refresh_token, token_type, user } }
        // Cần transform thành { accessToken, refreshToken, tokenType, user }
        if (response.data) {
          return {
            accessToken: response.data.access_token,
            refreshToken: response.data.refresh_token,
            tokenType: response.data.token_type,
            user: response.data.user,
          };
        }
        return response;
      },
    }),
    register: builder.mutation({
      query: (userData) => ({
        url: "auth/register",
        method: "POST",
        body: userData,
      }),
      transformResponse: (response) => {
        // API trả về { success, message, data: { access_token, refresh_token, token_type, user } }
        // Cần transform thành { accessToken, refreshToken, tokenType, user }
        if (response.data) {
          return {
            accessToken: response.data.access_token,
            refreshToken: response.data.refresh_token,
            tokenType: response.data.token_type,
            user: response.data.user,
          };
        }
        return response;
      },
    }),
    logout: builder.mutation({
      query: (refreshToken) => {
        const headers = {};
        if (refreshToken) {
          headers.authorization = `Bearer ${refreshToken}`;
        }
        return {
          url: "auth/logout",
          method: "POST",
          headers,
        };
      },
    }),
    logoutAll: builder.mutation({
      query: (refreshToken) => {
        const headers = {};
        if (refreshToken) {
          headers.authorization = `Bearer ${refreshToken}`;
        }
        return {
          url: "auth/logout-all",
          method: "POST",
          headers,
        };
      },
    }),
    refreshToken: builder.mutation({
      query: (refreshToken) => ({
        url: "auth/refresh",
        method: "POST",
        headers: {
          authorization: `Bearer ${refreshToken}`,
        },
      }),
      transformResponse: (response) => {
        // API trả về { access_token, refresh_token, token_type, user } hoặc có wrapper
        if (response.data) {
          return {
            accessToken:
              response.data.access_token || response.data.accessToken,
            refreshToken:
              response.data.refresh_token || response.data.refreshToken,
            tokenType: response.data.token_type || response.data.tokenType,
            user: response.data.user,
          };
        }
        // Nếu không có wrapper, kiểm tra snake_case hoặc camelCase
        if (response.access_token) {
          return {
            accessToken: response.access_token,
            refreshToken: response.refresh_token,
            tokenType: response.token_type,
            user: response.user,
          };
        }
        return response;
      },
    }),
  }),
});

export const {
  useLoginMutation,
  useRegisterMutation,
  useLogoutMutation,
  useLogoutAllMutation,
  useRefreshTokenMutation,
} = authApi;
