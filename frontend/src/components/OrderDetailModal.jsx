import React from "react";
import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faTimes,
  faSpinner,
  faExclamationTriangle,
} from "@fortawesome/free-solid-svg-icons";
import { useGetOrderItemsQuery } from "../app/orderApi";

const OrderDetailModal = ({ order, onClose }) => {
  const {
    data: items,
    error,
    isLoading,
  } = useGetOrderItemsQuery(order.id, {
    skip: !order,
  });

  const renderContent = () => {
    if (isLoading) {
      return (
        <div className="text-center py-8">
          <FontAwesomeIcon icon={faSpinner} spin size="2x" />
        </div>
      );
    }
    if (error) {
      return (
        <div className="text-center py-8 text-red-500">
          <FontAwesomeIcon icon={faExclamationTriangle} size="2x" />
          <p>Lỗi khi tải chi tiết đơn hàng.</p>
        </div>
      );
    }
    return (
      <ul className="space-y-3 max-h-60 overflow-y-auto pr-2">
        {items?.map((item) => (
          <li
            key={item.product_id}
            className="flex justify-between items-center bg-gray-50 p-3 rounded-lg"
          >
            <div>
              <p className="font-semibold">{item.product_name}</p>
              <p className="text-sm text-gray-500">
                {item.quantity} x {item.unit_price.toLocaleString("vi-VN")}đ
              </p>
            </div>
            <p className="font-semibold">
              {item.total_price.toLocaleString("vi-VN")}đ
            </p>
          </li>
        ))}
      </ul>
    );
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center z-50 p-4"
      onClick={onClose}
    >
      <motion.div
        initial={{ y: -50, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        exit={{ y: 50, opacity: 0 }}
        className="bg-white rounded-2xl shadow-2xl w-full max-w-lg p-8"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">
            Chi tiết Đơn hàng #{order.id}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <FontAwesomeIcon icon={faTimes} size="lg" />
          </button>
        </div>

        <div className="space-y-4 text-gray-700">
          <div className="grid grid-cols-2 gap-4">
            <p>
              <strong>Ngày đặt:</strong>{" "}
              {new Date(order.order_date).toLocaleString("vi-VN")}
            </p>
            <p>
              <strong>Trạng thái:</strong> {order.status}
            </p>
            <p>
              <strong>Mã khách hàng:</strong> {order.customer_id}
            </p>
            <p>
              <strong>Tổng tiền:</strong>{" "}
              <span className="font-bold text-indigo-600">
                {order.total_amount.toLocaleString("vi-VN")}đ
              </span>
            </p>
          </div>
          <div>
            <p>
              <strong>Địa chỉ giao hàng:</strong> {order.shipping_address}
            </p>
            <p>
              <strong>Số điện thoại:</strong> {order.phone_number}
            </p>
            {order.notes && (
              <p>
                <strong>Ghi chú:</strong> {order.notes}
              </p>
            )}
          </div>

          <div className="border-t pt-4">
            <h3 className="font-bold text-lg mb-2">Các sản phẩm</h3>
            {renderContent()}
          </div>
        </div>
      </motion.div>
    </motion.div>
  );
};

export default OrderDetailModal;
