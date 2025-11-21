import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useSelector } from "react-redux";
import { AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faTrash,
  faUsers,
  faTimes,
  faSpinner,
  faExclamationTriangle,
  faUserShield,
  faUser,
  faSearch,
  faEye,
  faArrowLeft,
  faHome,
} from "@fortawesome/free-solid-svg-icons";
import {
  useGetAllCustomersQuery,
  useGetCustomerByIdQuery,
  useUpdateCustomerRoleMutation,
  useDeleteCustomerMutation,
} from "../app/customerApi";
import InputField from "../components/ui/InputField";
import { motion } from "framer-motion";

// Modal để cập nhật role
const UpdateRoleModal = ({ customer, onClose, onSave, isLoading }) => {
  const [selectedRole, setSelectedRole] = useState(customer?.role || "USER");

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({ id: customer.id, role: selectedRole });
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
        className="bg-white rounded-2xl shadow-2xl w-full max-w-md p-8"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Cập nhật Role</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <FontAwesomeIcon icon={faTimes} size="lg" />
          </button>
        </div>

        <div className="mb-6">
          <div className="bg-gray-50 p-4 rounded-lg mb-4">
            <p className="text-sm text-gray-600 mb-1">Khách hàng</p>
            <p className="font-semibold text-gray-800">{customer?.email}</p>
            {customer?.full_name && (
              <p className="text-sm text-gray-600">{customer.full_name}</p>
            )}
          </div>

          <label className="block text-gray-700 font-semibold mb-2">
            Role <span className="text-red-500">*</span>
          </label>
          <select
            value={selectedRole}
            onChange={(e) => setSelectedRole(e.target.value)}
            className="w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-gray-50"
          >
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
          </select>
          <p className="mt-2 text-sm text-gray-500">
            Chọn role cho khách hàng này. ADMIN có quyền truy cập vào khu vực
            quản trị.
          </p>
        </div>

        <div className="flex justify-end pt-4 space-x-3">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            type="button"
            onClick={onClose}
            className="px-6 py-2 rounded-lg text-gray-700 bg-gray-100 hover:bg-gray-200 font-semibold transition-colors"
          >
            Hủy
          </motion.button>
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            type="button"
            onClick={handleSubmit}
            disabled={isLoading || selectedRole === customer?.role}
            className="px-6 py-2 rounded-lg bg-indigo-600 text-white font-semibold hover:bg-indigo-700 disabled:bg-indigo-300 disabled:cursor-not-allowed flex items-center transition-colors"
          >
            {isLoading && (
              <FontAwesomeIcon icon={faSpinner} spin className="mr-2" />
            )}
            Cập nhật
          </motion.button>
        </div>
      </motion.div>
    </motion.div>
  );
};

// Modal để xem chi tiết customer
const CustomerDetailModal = ({ customerId, onClose }) => {
  const {
    data: customerData,
    isLoading,
    error,
  } = useGetCustomerByIdQuery(customerId);

  const customer = customerData?.data || customerData;

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
        className="bg-white rounded-2xl shadow-2xl w-full max-w-2xl p-8 max-h-[90vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">
            Chi tiết Khách hàng
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <FontAwesomeIcon icon={faTimes} size="lg" />
          </button>
        </div>

        {isLoading ? (
          <div className="text-center py-8">
            <FontAwesomeIcon
              icon={faSpinner}
              spin
              size="2x"
              className="text-indigo-600"
            />
            <p className="mt-2 text-gray-600">Đang tải thông tin...</p>
          </div>
        ) : error ? (
          <div className="text-center py-8 text-red-500">
            <FontAwesomeIcon icon={faExclamationTriangle} size="2x" />
            <p className="mt-2">Lỗi khi tải thông tin khách hàng.</p>
          </div>
        ) : customer ? (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">ID</p>
                <p className="font-semibold text-gray-800">#{customer.id}</p>
              </div>
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">Role</p>
                <span
                  className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-semibold ${
                    customer.role === "ADMIN"
                      ? "bg-purple-100 text-purple-800"
                      : "bg-blue-100 text-blue-800"
                  }`}
                >
                  <FontAwesomeIcon
                    icon={customer.role === "ADMIN" ? faUserShield : faUser}
                    className="mr-2"
                  />
                  {customer.role}
                </span>
              </div>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <p className="text-sm text-gray-600 mb-1">Email</p>
              <p className="font-semibold text-gray-800">{customer.email}</p>
            </div>

            {customer.full_name && (
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">Họ và tên</p>
                <p className="font-semibold text-gray-800">
                  {customer.full_name}
                </p>
              </div>
            )}

            {customer.phone && (
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">Số điện thoại</p>
                <p className="font-semibold text-gray-800">{customer.phone}</p>
              </div>
            )}

            {customer.address && (
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">Địa chỉ</p>
                <p className="font-semibold text-gray-800">
                  {customer.address}
                </p>
              </div>
            )}
          </div>
        ) : null}

        <div className="flex justify-end pt-6">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={onClose}
            className="px-6 py-2 rounded-lg bg-indigo-600 text-white font-semibold hover:bg-indigo-700 transition-colors"
          >
            Đóng
          </motion.button>
        </div>
      </motion.div>
    </motion.div>
  );
};

// Modal xác nhận xóa
const DeleteConfirmModal = ({ customer, onClose, onConfirm, isLoading }) => {
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
        className="bg-white rounded-2xl shadow-2xl w-full max-w-md p-8"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="text-center mb-6">
          <div className="mx-auto flex items-center justify-center h-16 w-16 rounded-full bg-red-100 mb-4">
            <FontAwesomeIcon
              icon={faExclamationTriangle}
              className="text-red-600 text-2xl"
            />
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">
            Xác nhận xóa
          </h2>
          <p className="text-gray-600">
            Bạn có chắc chắn muốn xóa khách hàng này không?
          </p>
        </div>

        <div className="bg-gray-50 p-4 rounded-lg mb-6">
          <p className="text-sm text-gray-600 mb-1">Email</p>
          <p className="font-semibold text-gray-800">{customer?.email}</p>
          {customer?.full_name && (
            <>
              <p className="text-sm text-gray-600 mb-1 mt-2">Họ và tên</p>
              <p className="font-semibold text-gray-800">
                {customer.full_name}
              </p>
            </>
          )}
        </div>

        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
          <p className="text-sm text-yellow-800">
            <FontAwesomeIcon icon={faExclamationTriangle} className="mr-2" />
            Hành động này không thể hoàn tác. Khách hàng sẽ bị xóa vĩnh viễn
            khỏi hệ thống.
          </p>
        </div>

        <div className="flex justify-end space-x-3">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            type="button"
            onClick={onClose}
            disabled={isLoading}
            className="px-6 py-2 rounded-lg text-gray-700 bg-gray-100 hover:bg-gray-200 font-semibold transition-colors disabled:opacity-50"
          >
            Hủy
          </motion.button>
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            type="button"
            onClick={onConfirm}
            disabled={isLoading}
            className="px-6 py-2 rounded-lg bg-red-600 text-white font-semibold hover:bg-red-700 disabled:bg-red-300 disabled:cursor-not-allowed flex items-center transition-colors"
          >
            {isLoading && (
              <FontAwesomeIcon icon={faSpinner} spin className="mr-2" />
            )}
            Xóa
          </motion.button>
        </div>
      </motion.div>
    </motion.div>
  );
};

const CustomerAdminPage = () => {
  const navigate = useNavigate();
  const user = useSelector((state) => state.auth?.user);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [modalType, setModalType] = useState(null); // 'view', 'updateRole', 'delete'
  const [customerToDelete, setCustomerToDelete] = useState(null);

  const {
    data: customersData,
    error,
    isLoading: isLoadingCustomers,
  } = useGetAllCustomersQuery();

  const [updateCustomerRole, { isLoading: isUpdatingRole }] =
    useUpdateCustomerRoleMutation();
  const [deleteCustomer, { isLoading: isDeleting }] =
    useDeleteCustomerMutation();

  // Kiểm tra quyền ADMIN
  useEffect(() => {
    if (user && user.role !== "ADMIN") {
      navigate("/");
    }
  }, [user, navigate]);

  // Nếu chưa đăng nhập hoặc không phải ADMIN, không hiển thị gì
  if (!user || user.role !== "ADMIN") {
    return null;
  }

  const customers = customersData?.data || customersData || [];

  // Lọc customers theo search term
  const filteredCustomers = customers.filter((customer) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      customer.email?.toLowerCase().includes(searchLower) ||
      customer.full_name?.toLowerCase().includes(searchLower) ||
      customer.id?.toString().includes(searchLower)
    );
  });

  const handleOpenViewModal = (customer) => {
    setSelectedCustomer(customer);
    setModalType("view");
  };

  const handleOpenUpdateRoleModal = (customer) => {
    setSelectedCustomer(customer);
    setModalType("updateRole");
  };

  const handleOpenDeleteModal = (customer) => {
    setCustomerToDelete(customer);
    setModalType("delete");
  };

  const handleCloseModal = () => {
    setModalType(null);
    setSelectedCustomer(null);
    setCustomerToDelete(null);
  };

  const handleUpdateRole = async ({ id, role }) => {
    try {
      await updateCustomerRole({ id, role }).unwrap();
      handleCloseModal();
    } catch (err) {
      console.error("Failed to update role:", err);
      // Có thể thêm toast notification ở đây
    }
  };

  const handleDelete = async () => {
    if (!customerToDelete) return;
    try {
      await deleteCustomer(customerToDelete.id).unwrap();
      handleCloseModal();
    } catch (err) {
      console.error("Failed to delete customer:", err);
      // Có thể thêm toast notification ở đây
    }
  };

  const renderCustomerRows = () => {
    if (isLoadingCustomers) {
      return (
        <tr>
          <td colSpan="6" className="text-center py-16">
            <FontAwesomeIcon
              icon={faSpinner}
              spin
              size="2x"
              className="text-indigo-600"
            />
            <p className="mt-2 text-gray-600">
              Đang tải danh sách khách hàng...
            </p>
          </td>
        </tr>
      );
    }

    if (error) {
      return (
        <tr>
          <td colSpan="6" className="text-center py-16 text-red-500">
            <FontAwesomeIcon icon={faExclamationTriangle} size="2x" />
            <p className="mt-2">Lỗi khi tải dữ liệu.</p>
            <p className="text-sm mt-1">
              {error?.data?.message || "Vui lòng thử lại sau."}
            </p>
          </td>
        </tr>
      );
    }

    if (filteredCustomers.length === 0) {
      return (
        <tr>
          <td colSpan="6" className="text-center py-16 text-gray-500">
            <FontAwesomeIcon icon={faUsers} size="2x" />
            <p className="mt-2">
              {searchTerm
                ? "Không tìm thấy khách hàng nào."
                : "Chưa có khách hàng nào."}
            </p>
          </td>
        </tr>
      );
    }

    return filteredCustomers.map((customer) => (
      <motion.tr
        key={customer.id}
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="hover:bg-gray-50 transition-colors"
      >
        <td className="p-4 border-b border-gray-200 font-mono text-gray-700">
          #{customer.id}
        </td>
        <td className="p-4 border-b border-gray-200">
          <div>
            <p className="font-medium text-gray-800">{customer.email}</p>
            {customer.full_name && (
              <p className="text-sm text-gray-500">{customer.full_name}</p>
            )}
          </div>
        </td>
        <td className="p-4 border-b border-gray-200">
          <span
            className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${
              customer.role === "ADMIN"
                ? "bg-purple-100 text-purple-800"
                : "bg-blue-100 text-blue-800"
            }`}
          >
            <FontAwesomeIcon
              icon={customer.role === "ADMIN" ? faUserShield : faUser}
              className="mr-1"
            />
            {customer.role}
          </span>
        </td>
        <td className="p-4 border-b border-gray-200 text-gray-600">
          {customer.phone || "-"}
        </td>
        <td className="p-4 border-b border-gray-200">
          <div className="flex space-x-2">
            <motion.button
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
              onClick={() => handleOpenViewModal(customer)}
              className="text-indigo-600 hover:text-indigo-800 transition-colors"
              aria-label="View Details"
              title="Xem chi tiết"
            >
              <FontAwesomeIcon icon={faEye} />
            </motion.button>
            <motion.button
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
              onClick={() => handleOpenUpdateRoleModal(customer)}
              className="text-indigo-600 hover:text-indigo-800 transition-colors"
              aria-label="Edit Role"
              title="Cập nhật role"
            >
              <FontAwesomeIcon icon={faEdit} />
            </motion.button>
            <motion.button
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
              onClick={() => handleOpenDeleteModal(customer)}
              disabled={isDeleting}
              className="text-red-600 hover:text-red-800 disabled:text-gray-300 transition-colors"
              aria-label="Delete"
              title="Xóa khách hàng"
            >
              <FontAwesomeIcon icon={faTrash} />
            </motion.button>
          </div>
        </td>
      </motion.tr>
    ));
  };

  return (
    <>
      <div className="p-8 bg-gray-50 min-h-screen">
        {/* Breadcrumb Navigation */}
        <div className="mb-6">
          <nav className="flex items-center space-x-2 text-sm text-gray-600">
            <Link
              to="/"
              className="hover:text-indigo-600 transition-colors flex items-center"
            >
              <FontAwesomeIcon icon={faHome} className="mr-1" />
              Trang chủ
            </Link>
            <span>/</span>
            <Link
              to="/admin"
              className="hover:text-indigo-600 transition-colors"
            >
              Quản trị
            </Link>
            <span>/</span>
            <span className="text-gray-800 font-semibold">
              Quản lý Khách hàng
            </span>
          </nav>
        </div>

        {/* Header with back button */}
        <div className="mb-8">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center space-x-4">
              <Link to="/admin">
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className="flex items-center space-x-2 px-4 py-2 rounded-lg bg-white text-gray-700 hover:bg-gray-100 shadow-md transition-colors"
                >
                  <FontAwesomeIcon icon={faArrowLeft} />
                  <span>Quay lại</span>
                </motion.button>
              </Link>
            </div>
          </div>
          <h1 className="text-3xl font-bold text-gray-800 mb-2">
            Quản lý Khách hàng
          </h1>
          <p className="text-gray-600">
            Quản lý tài khoản và quyền truy cập của khách hàng
          </p>
        </div>

        {/* Search bar */}
        <div className="mb-6">
          <div className="relative">
            <FontAwesomeIcon
              icon={faSearch}
              className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400"
            />
            <input
              type="text"
              placeholder="Tìm kiếm theo email, tên hoặc ID..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
            />
          </div>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-white p-6 rounded-xl shadow-md"
          >
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Tổng khách hàng</p>
                <p className="text-2xl font-bold text-gray-800">
                  {customers.length}
                </p>
              </div>
              <FontAwesomeIcon
                icon={faUsers}
                className="text-3xl text-indigo-500"
              />
            </div>
          </motion.div>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
            className="bg-white p-6 rounded-xl shadow-md"
          >
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Admin</p>
                <p className="text-2xl font-bold text-gray-800">
                  {customers.filter((c) => c.role === "ADMIN").length}
                </p>
              </div>
              <FontAwesomeIcon
                icon={faUserShield}
                className="text-3xl text-purple-500"
              />
            </div>
          </motion.div>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
            className="bg-white p-6 rounded-xl shadow-md"
          >
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Người dùng</p>
                <p className="text-2xl font-bold text-gray-800">
                  {customers.filter((c) => c.role === "USER").length}
                </p>
              </div>
              <FontAwesomeIcon
                icon={faUser}
                className="text-3xl text-blue-500"
              />
            </div>
          </motion.div>
        </div>

        {/* Table */}
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead className="bg-gray-100">
                <tr>
                  <th className="p-4 font-semibold text-gray-600">ID</th>
                  <th className="p-4 font-semibold text-gray-600">
                    Email / Tên
                  </th>
                  <th className="p-4 font-semibold text-gray-600">Role</th>
                  <th className="p-4 font-semibold text-gray-600">
                    Số điện thoại
                  </th>
                  <th className="p-4 font-semibold text-gray-600">Hành động</th>
                </tr>
              </thead>
              <tbody>{renderCustomerRows()}</tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Modals */}
      <AnimatePresence>
        {modalType === "view" && selectedCustomer && (
          <CustomerDetailModal
            customerId={selectedCustomer.id}
            onClose={handleCloseModal}
          />
        )}
        {modalType === "updateRole" && selectedCustomer && (
          <UpdateRoleModal
            customer={selectedCustomer}
            onClose={handleCloseModal}
            onSave={handleUpdateRole}
            isLoading={isUpdatingRole}
          />
        )}
        {modalType === "delete" && customerToDelete && (
          <DeleteConfirmModal
            customer={customerToDelete}
            onClose={handleCloseModal}
            onConfirm={handleDelete}
            isLoading={isDeleting}
          />
        )}
      </AnimatePresence>
    </>
  );
};

export default CustomerAdminPage;
