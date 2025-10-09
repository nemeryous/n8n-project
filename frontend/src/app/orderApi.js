import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export const orderApi = createApi({
  reducerPath: "orderApi",
  baseQuery: fetchBaseQuery({ baseUrl: BASE_URL }),
  tagTypes: ["Order"],
  endpoints: (builder) => ({
    createOrder: builder.mutation({
      query: (newOrder) => ({
        url: "orders",
        method: "POST",
        body: newOrder,
      }),
      // After creating an order, we might want to invalidate cart-related tags
      // to refetch cart data and show it as empty.
      invalidatesTags: ["Cart", "CartItem"],
    }),
  }),
});

export const { useCreateOrderMutation } = orderApi;
