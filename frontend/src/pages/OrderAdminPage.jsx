import React, { useState } from "react";
import {
  useGetOrdersQuery,
  useUpdateOrderStatusMutation,
} from "../app/orderApi";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faSpinner,
  faExclamationTriangle,
  faBoxOpen,
  faEye,
  faCheckCircle,
  faTruck,
  faBan,
} from "@fortawesome/free-solid-svg-icons";
import { AnimatePresence } from "framer-motion";
import OrderDetailModal from "../components/OrderDetailModal";

const statusOptions = [
  "PENDING",
  "CONFIRMED",
  "PROCESSING",
  "DELIVERED",
  "REFUNDED",
  "ABANDONED",
];

const statusStyles = {
  PENDING: "bg-yellow-100 text-yellow-800",
  CONFIRMED: "bg-blue-100 text-blue-800",
  PROCESSING: "bg-purple-100 text-purple-800",
  DELIVERED: "bg-green-100 text-green-800",
  REFUNDED: "bg-red-100 text-red-800",
  ABANDONED: "bg-gray-100 text-gray-800",
};

const statusIcons = {
  PENDING: faSpinner,
  CONFIRMED: faCheckCircle,
  PROCESSING: faTruck,
  DELIVERED: faCheckCircle,
  REFUNDED: faBan,
  ABANDONED: faBan,
};

const OrderAdminPage = () => {
  const [selectedStatus, setSelectedStatus] = useState("PENDING");
  const [selectedOrder, setSelectedOrder] = useState(null);
  const {
    data: ordersData,
    error,
    isLoading,
    refetch,
  } = useGetOrdersQuery(selectedStatus);
  const [updateOrderStatus, { isLoading: isUpdating }] =
    useUpdateOrderStatusMutation();

  const handleStatusChange = async (orderId, newStatus) => {
    try {
      await updateOrderStatus({ orderId, status: newStatus }).unwrap();
      refetch();
    } catch (err) {
      console.error("Failed to update order status:", err);
    }
  };

  const renderOrderRows = () => {
    if (isLoading) {
      return (
        <tr>
          <td colSpan="7" className="text-center py-16">
            <FontAwesomeIcon icon={faSpinner} spin size="2x" />
            <p className="mt-2">Đang tải danh sách đơn hàng...</p>
          </td>
        </tr>
      );
    }

    if (error) {
      return (
        <tr>
          <td colSpan="7" className="text-center py-16 text-red-500">
            <FontAwesomeIcon icon={faExclamationTriangle} size="2x" />
            <p className="mt-2">Lỗi khi tải dữ liệu.</p>
          </td>
        </tr>
      );
    }

    if (!ordersData || ordersData.length === 0) {
      return (
        <tr>
          <td colSpan="7" className="text-center py-16 text-gray-500">
            <FontAwesomeIcon icon={faBoxOpen} size="2x" />
            <p className="mt-2">Không có đơn hàng nào.</p>
          </td>
        </tr>
      );
    }

    return ordersData.map((order) => (
      <tr key={order.id} className="hover:bg-gray-50 transition-colors">
        <td className="p-4 border-b border-gray-200 font-mono text-gray-700">
          #{order.id}
        </td>
        <td className="p-4 border-b border-gray-200">{order.customer_id}</td>
        <td className="p-4 border-b border-gray-200">
          {new Date(order.order_date).toLocaleDateString("vi-VN")}
        </td>
        <td className="p-4 border-b border-gray-200 font-semibold">
          {order.total_amount.toLocaleString("vi-VN")}đ
        </td>
        <td className="p-4 border-b border-gray-200">
          <span
            className={`px-3 py-1 rounded-full text-xs font-semibold ${
              statusStyles[order.status]
            }`}
          >
            <FontAwesomeIcon
              icon={statusIcons[order.status]}
              className="mr-2"
            />
            {order.status}
          </span>
        </td>
        <td className="p-4 border-b border-gray-200">
          <select
            value={order.status}
            onChange={(e) => handleStatusChange(order.id, e.target.value)}
            disabled={isUpdating}
            className="p-2 border rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            {statusOptions.map((status) => (
              <option key={status} value={status}>
                {status}
              </option>
            ))}
          </select>
        </td>
        <td className="p-4 border-b border-gray-200">
          <motion.button
            whileHover={{ scale: 1.1 }}
            onClick={() => setSelectedOrder(order)}
            className="text-indigo-600 hover:text-indigo-800"
            aria-label="View Details"
          >
            <FontAwesomeIcon icon={faEye} />
          </motion.button>
        </td>
      </tr>
    ));
  };

  return (
    <>
      <div className="p-8 bg-gray-50 min-h-full">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">
          Quản lý Đơn hàng
        </h1>

        <div className="mb-6">
          <div className="flex space-x-2 border-b-2 border-gray-200">
            {statusOptions.map((status) => (
              <button
                key={status}
                onClick={() => setSelectedStatus(status)}
                className={`px-4 py-2 font-semibold text-sm transition-colors ${
                  selectedStatus === status
                    ? "border-b-2 border-indigo-600 text-indigo-600"
                    : "text-gray-500 hover:text-indigo-500"
                }`}
              >
                {status}
              </button>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead className="bg-gray-100">
                <tr>
                  <th className="p-4 font-semibold text-gray-600">Mã ĐH</th>
                  <th className="p-4 font-semibold text-gray-600">
                    Mã Khách hàng
                  </th>
                  <th className="p-4 font-semibold text-gray-600">Ngày đặt</th>
                  <th className="p-4 font-semibold text-gray-600">Tổng tiền</th>
                  <th className="p-4 font-semibold text-gray-600">
                    Trạng thái
                  </th>
                  <th className="p-4 font-semibold text-gray-600">
                    Cập nhật TT
                  </th>
                  <th className="p-4 font-semibold text-gray-600">Chi tiết</th>
                </tr>
              </thead>
              <tbody>{renderOrderRows()}</tbody>
            </table>
          </div>
        </div>
      </div>
      <AnimatePresence>
        {selectedOrder && (
          <OrderDetailModal
            order={selectedOrder}
            onClose={() => setSelectedOrder(null)}
          />
        )}
      </AnimatePresence>
    </>
  );
};

export default OrderAdminPage;
