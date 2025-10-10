import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const cartApi = createApi({
  reducerPath: "cartApi",
  baseQuery: fetchBaseQuery({ baseUrl: import.meta.env.VITE_BASE_URL }),
  tagTypes: ["Cart"],
  endpoints: (builder) => ({
    // Lấy hoặc tạo giỏ hàng cho khách hàng đã đăng nhập
    getOrCreateCartByCustomer: builder.query({
      query: (customerId) => `carts/customer/${customerId}/current`,
      providesTags: (result, error, customerId) => [
        { type: "Cart", id: customerId },
      ],
    }),
    // Lấy giỏ hàng đang hoạt động của khách hàng
    getActiveCartByCustomerId: builder.query({
      query: (customerId) => `carts/customer/${customerId}/active`,
      providesTags: (result, error, customerId) => [
        { type: "Cart", id: customerId },
      ],
    }),
    // Lấy giỏ hàng bằng ID
    getCartById: builder.query({
      query: (id) => `carts/${id}`,
      providesTags: (result, error, id) => [{ type: "Cart", id }],
    }),
    // Các endpoints khác có thể thêm ở đây (create, update, delete cart)
  }),
});

export const {
  useGetOrCreateCartByCustomerQuery,
  useGetActiveCartByCustomerIdQuery,
  useGetCartByIdQuery,
} = cartApi;
