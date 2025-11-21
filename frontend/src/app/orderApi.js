import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export const orderApi = createApi({
  reducerPath: "orderApi",
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
  tagTypes: ["Orders"],
  endpoints: (builder) => ({
    createOrder: builder.mutation({
      query: (newOrder) => ({
        url: "orders",
        method: "POST",
        body: newOrder,
      }),
      invalidatesTags: ["Cart", "CartItem"],
      transformResponse: (response) => {
        // API có thể trả về { success, data: {...} } hoặc trực tiếp OrderDto
        const orderData = response.data || response;
        if (!orderData) return response;

        // Convert snake_case to camelCase
        return {
          id: orderData.id,
          customerId: orderData.customer_id,
          cartId: orderData.cart_id,
          orderDate: orderData.order_date,
          status: orderData.status,
          totalAmount: orderData.total_amount,
          shippingAddress: orderData.shipping_address,
          phoneNumber: orderData.phone_number,
          notes: orderData.notes,
          abandonedAt: orderData.abandoned_at,
          createdAt: orderData.created_at,
          updatedAt: orderData.updated_at,
        };
      },
    }),
    getOrders: builder.query({
      query: (status) => ({
        url: "/orders",
        params: status ? { status } : {},
      }),
      providesTags: ["Orders"],
      transformResponse: (response) => {
        // API có thể trả về { success, data: [...] } hoặc trực tiếp List<OrderDto>
        const orders = response.data || response || [];
        if (!Array.isArray(orders)) return orders;

        return orders.map((order) => ({
          id: order.id,
          customerId: order.customer_id,
          cartId: order.cart_id,
          orderDate: order.order_date,
          status: order.status,
          totalAmount: order.total_amount,
          shippingAddress: order.shipping_address,
          phoneNumber: order.phone_number,
          notes: order.notes,
          abandonedAt: order.abandoned_at,
          createdAt: order.created_at,
          updatedAt: order.updated_at,
        }));
      },
    }),
    getOrderById: builder.query({
      query: (orderId) => `/orders/${orderId}`,
      providesTags: (result, error, orderId) => [
        { type: "Orders", id: orderId },
      ],
      transformResponse: (response) => {
        const orderData = response.data || response;
        if (!orderData) return response;

        return {
          id: orderData.id,
          customerId: orderData.customer_id,
          cartId: orderData.cart_id,
          orderDate: orderData.order_date,
          status: orderData.status,
          totalAmount: orderData.total_amount,
          shippingAddress: orderData.shipping_address,
          phoneNumber: orderData.phone_number,
          notes: orderData.notes,
          abandonedAt: orderData.abandoned_at,
          createdAt: orderData.created_at,
          updatedAt: orderData.updated_at,
        };
      },
    }),
    getOrderItems: builder.query({
      query: (orderId) => `/orders/${orderId}/items`,
      transformResponse: (response) => {
        const items = response.data || response || [];
        if (!Array.isArray(items)) return items;

        return items.map((item) => ({
          productId: item.product_id,
          productName: item.product_name,
          quantity: item.quantity,
          unitPrice: item.unit_price,
          totalPrice: item.total_price,
          // Giữ nguyên các field khác nếu có
          ...item,
        }));
      },
    }),
    updateOrderStatus: builder.mutation({
      query: ({ orderId, status }) => ({
        url: `/orders/${orderId}/status`,
        method: "PUT",
        body: { status },
      }),
      invalidatesTags: (result, error, { orderId }) => [
        "Orders",
        { type: "Orders", id: orderId },
      ],
      transformResponse: (response) => {
        const orderData = response.data || response;
        if (!orderData) return response;

        return {
          id: orderData.id,
          customerId: orderData.customer_id,
          cartId: orderData.cart_id,
          orderDate: orderData.order_date,
          status: orderData.status,
          totalAmount: orderData.total_amount,
          shippingAddress: orderData.shipping_address,
          phoneNumber: orderData.phone_number,
          notes: orderData.notes,
          abandonedAt: orderData.abandoned_at,
          createdAt: orderData.created_at,
          updatedAt: orderData.updated_at,
        };
      },
    }),
  }),
});

export const {
  useCreateOrderMutation,
  useGetOrdersQuery,
  useGetOrderByIdQuery,
  useGetOrderItemsQuery,
  useUpdateOrderStatusMutation,
} = orderApi;
