import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export const customerApi = createApi({
  reducerPath: "customerApi",
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
  tagTypes: ["Customers"],
  endpoints: (builder) => ({
    getAllCustomers: builder.query({
      query: () => ({
        url: "admin/customers",
      }),
      providesTags: ["Customers"],
      transformResponse: (response) => {
        // API trả về { success, message, data: [...] }
        return response?.data || response;
      },
    }),
    getCustomerById: builder.query({
      query: (id) => ({
        url: `admin/customers/${id}`,
      }),
      providesTags: (result, error, id) => [{ type: "Customers", id }],
      transformResponse: (response) => {
        // API trả về { success, message, data: {...} }
        return response?.data || response;
      },
    }),
    updateCustomerRole: builder.mutation({
      query: ({ id, role }) => ({
        url: `admin/customers/${id}/role`,
        method: "PUT",
        body: { role },
      }),
      invalidatesTags: (result, error, { id }) => [
        "Customers",
        { type: "Customers", id },
      ],
      transformResponse: (response) => {
        // API trả về { success, message, data: {...} }
        return response?.data || response;
      },
    }),
    deleteCustomer: builder.mutation({
      query: (id) => ({
        url: `admin/customers/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["Customers"],
    }),
  }),
});

export const {
  useGetAllCustomersQuery,
  useGetCustomerByIdQuery,
  useUpdateCustomerRoleMutation,
  useDeleteCustomerMutation,
} = customerApi;
