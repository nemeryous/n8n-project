import React, { useState } from "react";
import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faBoxOpen,
  faCheck,
  faTruck,
  faRotateLeft,
  faSpinner,
} from "@fortawesome/free-solid-svg-icons";

// Mock data - Trong production sẽ fetch từ API
const mockOrders = [
  {
    id: "1001",
    customer_name: "Nguyễn Văn A",
    customer_email: "email@example.com",
    date: "07/10/2025",
    total: 580000,
    status: "pending",
    address: "123 Đường ABC, Quận 1, TP.HCM",
  },
  {
    id: "1000",
    customer_name: "Trần Thị B",
    customer_email: "tran.b@example.com",
    date: "06/10/2025",
    total: 1200000,
    status: "delivered",
    address: "456 Đường XYZ, Quận 2, TP.HCM",
  },
  {
    id: "999",
    customer_name: "Khách vãng lai",
    customer_email: "guest@example.com",
    date: "05/10/2025",
    total: 450000,
    status: "refunded",
    address: "789 Đường DEF, Quận 3, TP.HCM",
  },
];

const AdminPage = () => {
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [newStatus, setNewStatus] = useState("");

  const getStatusColor = (status) => {
    switch (status) {
      case "pending":
        return "bg-yellow-100 text-yellow-800";
      case "confirmed":
        return "bg-blue-100 text-blue-800";
      case "processing":
        return "bg-purple-100 text-purple-800";
      case "delivered":
        return "bg-green-100 text-green-800";
      case "refunded":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "pending":
        return faBoxOpen;
      case "confirmed":
        return faCheck;
      case "processing":
        return faSpinner;
      case "delivered":
        return faTruck;
      case "refunded":
        return faRotateLeft;
      default:
        return faBoxOpen;
    }
  };

  const handleUpdateStatus = () => {
    if (!selectedOrder || !newStatus) return;

    // Trong production: Gọi API để cập nhật trạng thái
    console.log(`Updating order ${selectedOrder.id} to status: ${newStatus}`);

    // Mock update
    selectedOrder.status = newStatus;
    setSelectedOrder({ ...selectedOrder });
    setNewStatus("");
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <motion.div
              whileHover={{ scale: 1.05 }}
              className="text-3xl font-bold tracking-tight cursor-pointer"
            >
              <span className="bg-gradient-to-r from-indigo-600 to-purple-600 text-transparent bg-clip-text">
                Fashion
              </span>
              <span className="text-yellow-500">Hub</span>
              <span className="ml-2 text-sm text-gray-500">Admin</span>
            </motion.div>

            <nav className="flex items-center space-x-8">
              <a
                href="#orders"
                className="font-medium text-indigo-600 hover:text-indigo-700"
              >
                Quản lý Đơn hàng
              </a>
              <a
                href="#products"
                className="font-medium text-gray-500 hover:text-gray-700"
              >
                Quản lý Sản phẩm
              </a>
              <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                className="px-4 py-2 rounded-lg text-red-600 font-medium hover:bg-red-50"
              >
                Đăng xuất
              </motion.button>
            </nav>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-4 py-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <h1 className="text-3xl font-bold text-gray-800 mb-8">
            Quản lý Đơn hàng
          </h1>

          {/* Order List */}
          <div className="bg-white rounded-xl shadow-lg overflow-hidden mb-8">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Khách hàng
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ngày đặt
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Tổng tiền
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Trạng thái
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {mockOrders.map((order) => (
                  <motion.tr
                    key={order.id}
                    whileHover={{ backgroundColor: "#f9fafb" }}
                    className="cursor-pointer"
                    onClick={() => setSelectedOrder(order)}
                  >
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      #{order.id}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {order.customer_name}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {order.date}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-medium">
                      {order.total.toLocaleString("vi-VN")}đ
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span
                        className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
                          order.status
                        )}`}
                      >
                        <FontAwesomeIcon
                          icon={getStatusIcon(order.status)}
                          className="mr-1"
                        />
                        {order.status.charAt(0).toUpperCase() +
                          order.status.slice(1)}
                      </span>
                    </td>
                  </motion.tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Order Details */}
          {selectedOrder && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="bg-white rounded-xl shadow-lg p-6"
            >
              <h2 className="text-2xl font-bold text-gray-800 mb-6">
                Chi tiết đơn hàng #{selectedOrder.id}
              </h2>

              <div className="grid md:grid-cols-2 gap-6 mb-8">
                <div>
                  <p className="text-gray-600 mb-2">
                    <strong>Khách hàng:</strong> {selectedOrder.customer_name}
                  </p>
                  <p className="text-gray-600 mb-2">
                    <strong>Email:</strong> {selectedOrder.customer_email}
                  </p>
                  <p className="text-gray-600">
                    <strong>Địa chỉ:</strong> {selectedOrder.address}
                  </p>
                </div>

                <div className="flex flex-col md:items-end">
                  <div className="flex items-center space-x-4 mb-4">
                    <label className="text-gray-700 font-medium">
                      Cập nhật trạng thái:
                    </label>
                    <select
                      value={newStatus}
                      onChange={(e) => setNewStatus(e.target.value)}
                      className="form-select rounded-lg border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
                    >
                      <option value="">Chọn trạng thái</option>
                      <option value="pending">Pending</option>
                      <option value="confirmed">Confirmed</option>
                      <option value="processing">Processing</option>
                      <option value="delivered">Delivered</option>
                      <option value="refunded">Refunded</option>
                    </select>
                  </div>

                  <motion.button
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    onClick={handleUpdateStatus}
                    disabled={!newStatus}
                    className={`px-6 py-2 rounded-lg font-medium shadow-md ${
                      newStatus
                        ? "bg-indigo-600 text-white hover:bg-indigo-700"
                        : "bg-gray-100 text-gray-400 cursor-not-allowed"
                    }`}
                  >
                    Cập nhật
                  </motion.button>
                </div>
              </div>
            </motion.div>
          )}
        </motion.div>
      </main>
    </div>
  );
};

export default AdminPage;
