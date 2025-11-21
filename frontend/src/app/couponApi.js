import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export const couponApi = createApi({
  reducerPath: "couponApi",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
    prepareHeaders: (headers, { getState }) => {
      const token = getState().auth?.accessToken;
      if (token) {
        headers.set("authorization", `Bearer ${token}`);
      }
      headers.set("ngrok-skip-browser-warning", "true");
      return headers;
    },
  }),
  tagTypes: ["Coupons"],
  endpoints: (builder) => ({
    // Lấy tất cả coupons (ADMIN only)
    getAllCoupons: builder.query({
      query: () => "coupons",
      providesTags: ["Coupons"],
      transformResponse: (response) => {
        // API trả về { success, data: [...] }
        return response.data || response;
      },
    }),
    // Lấy coupon theo ID (ADMIN only)
    getCouponById: builder.query({
      query: (id) => `coupons/${id}`,
      providesTags: (result, error, id) => [{ type: "Coupons", id }],
      transformResponse: (response) => {
        return response.data || response;
      },
    }),
    // Lấy coupon theo code (Public)
    getCouponByCode: builder.query({
      query: (code) => `coupons/code/${code}`,
      transformResponse: (response) => {
        return response.data || response;
      },
    }),
    // Validate coupon (Authenticated)
    validateCoupon: builder.query({
      query: ({ code, amount, productIds, categories, customerId }) => ({
        url: "coupons/validate",
        params: {
          code,
          amount,
          ...(productIds && productIds.length > 0 && { productIds }),
          ...(categories && categories.length > 0 && { categories }),
          ...(customerId && { customerId }),
        },
      }),
      transformResponse: (response) => {
        const data = response.data || response;
        if (!data) return response;

        // Convert snake_case to camelCase
        return {
          isValid: data.is_valid,
          discountAmount: data.discount_amount,
          finalAmount: data.final_amount,
          message: data.message,
          // Keep original fields for backward compatibility
          is_valid: data.is_valid,
          discount_amount: data.discount_amount,
          final_amount: data.final_amount,
        };
      },
    }),
    // Lấy danh sách coupon available (Public)
    getAvailableCoupons: builder.query({
      query: (segment) => ({
        url: "coupons/available",
        params: segment ? { segment } : {},
      }),
      transformResponse: (response) => {
        return response.data || response;
      },
    }),
    // Tạo coupon mới (ADMIN only)
    createCoupon: builder.mutation({
      query: (couponData) => ({
        url: "coupons",
        method: "POST",
        body: couponData,
      }),
      invalidatesTags: ["Coupons"],
      transformResponse: (response) => {
        return response.data || response;
      },
    }),
    // Cập nhật coupon (ADMIN only)
    updateCoupon: builder.mutation({
      query: ({ id, ...couponData }) => ({
        url: `coupons/${id}`,
        method: "PUT",
        body: couponData,
      }),
      invalidatesTags: (result, error, { id }) => [
        "Coupons",
        { type: "Coupons", id },
      ],
      transformResponse: (response) => {
        return response.data || response;
      },
    }),
    // Xóa coupon (ADMIN only)
    deleteCoupon: builder.mutation({
      query: (id) => ({
        url: `coupons/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["Coupons"],
    }),
  }),
});

export const {
  useGetAllCouponsQuery,
  useGetCouponByIdQuery,
  useGetCouponByCodeQuery,
  useValidateCouponQuery,
  useGetAvailableCouponsQuery,
  useCreateCouponMutation,
  useUpdateCouponMutation,
  useDeleteCouponMutation,
} = couponApi;
