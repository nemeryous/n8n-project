import React from "react";
import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faBoxOpen,
  faTshirt,
  faUsers,
} from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";

const AdminPage = () => {
  const NavLink = ({ to, icon, title, subtitle }) => (
    <motion.div
      whileHover={{ y: -5, scale: 1.02 }}
      className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300"
    >
      <Link to={to} className="flex flex-col items-center text-center">
        <FontAwesomeIcon
          icon={icon}
          className="text-4xl text-indigo-500 mb-4"
        />
        <h3 className="text-xl font-bold text-gray-800">{title}</h3>
        <p className="text-gray-500 mt-1">{subtitle}</p>
      </Link>
    </motion.div>
  );

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <header className="mb-12 text-center">
        <h1 className="text-5xl font-extrabold text-gray-900">
          Trang Quản Trị
        </h1>
        <p className="text-lg text-gray-600 mt-2">
          Chào mừng đến với khu vực quản lý của FashionHub
        </p>
      </header>

      <main className="max-w-4xl mx-auto">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <NavLink
            to="/admin/products"
            icon={faTshirt}
            title="Quản lý Sản phẩm"
            subtitle="Thêm, sửa, xóa sản phẩm"
          />
          <NavLink
            to="/admin/orders"
            icon={faBoxOpen}
            title="Quản lý Đơn hàng"
            subtitle="Xem và cập nhật đơn hàng"
          />
          <NavLink
            to="/admin/users"
            icon={faUsers}
            title="Quản lý Người dùng"
            subtitle="Quản lý tài khoản"
          />
        </div>
      </main>
    </div>
  );
};

export default AdminPage;
