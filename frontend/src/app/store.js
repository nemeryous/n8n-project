import { configureStore } from "@reduxjs/toolkit";
import { productApi } from "./productApi";
import { cartApi } from "./cartApi";
import { cartItemApi } from "./cartItemApi";
import { orderApi } from "./orderApi";
import { authApi } from "./authApi";
import authReducer from "./authSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    [productApi.reducerPath]: productApi.reducer,
    [cartApi.reducerPath]: cartApi.reducer,
    [cartItemApi.reducerPath]: cartItemApi.reducer,
    [orderApi.reducerPath]: orderApi.reducer,
    [authApi.reducerPath]: authApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(
      productApi.middleware,
      cartApi.middleware,
      cartItemApi.middleware,
      orderApi.middleware,
      authApi.middleware,
    ),
});
