import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export const cartItemApi = createApi({
  reducerPath: "cartItemApi",
  baseQuery: fetchBaseQuery({ baseUrl: BASE_URL }),
  tagTypes: ["CartItem"],
  endpoints: (builder) => ({
    getCartItemsByCustomerId: builder.query({
      query: (customerId) => `cart-items/customer/${customerId}`,
      providesTags: ["CartItem"],
    }),
    createCartItem: builder.mutation({
      query: (cartItem) => ({
        url: "cart-items",
        method: "POST",
        body: cartItem,
      }),
      invalidatesTags: ["CartItem"],
    }),
    updateCartItem: builder.mutation({
      query: ({ id, ...cartItem }) => ({
        url: `cart-items/${id}`,
        method: "PUT",
        body: cartItem,
      }),
      invalidatesTags: ["CartItem"],
    }),
    deleteCartItem: builder.mutation({
      query: (id) => ({
        url: `cart-items/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["CartItem"],
    }),
  }),
});

export const {
  useGetCartItemsByCustomerIdQuery,
  useCreateCartItemMutation,
  useUpdateCartItemMutation,
  useDeleteCartItemMutation,
} = cartItemApi;
