import { useState } from "react";
import { Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faPlus,
  faEdit,
  faTrash,
  faArrowLeft,
  faSpinner,
  faExclamationTriangle,
  faCheckCircle,
  faTimesCircle,
  faTag,
} from "@fortawesome/free-solid-svg-icons";
import {
  useGetAllCouponsQuery,
  useCreateCouponMutation,
  useUpdateCouponMutation,
  useDeleteCouponMutation,
} from "../app/couponApi";
import CouponFormModal from "../components/admin/CouponFormModal";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";

const CouponAdminPage = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedCoupon, setSelectedCoupon] = useState(null);
  const [deleteConfirm, setDeleteConfirm] = useState(null);

  const { data: coupons = [], isLoading, error } = useGetAllCouponsQuery();
  const [createCoupon, { isLoading: isCreating }] = useCreateCouponMutation();
  const [updateCoupon, { isLoading: isUpdating }] = useUpdateCouponMutation();
  const [deleteCoupon, { isLoading: isDeleting }] = useDeleteCouponMutation();

  const handleOpenModal = (coupon = null) => {
    setSelectedCoupon(coupon);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedCoupon(null);
  };

  const handleSave = async (couponData) => {
    try {
      if (selectedCoupon) {
        await updateCoupon({ id: selectedCoupon.id, ...couponData }).unwrap();
      } else {
        await createCoupon(couponData).unwrap();
      }
      handleCloseModal();
    } catch (err) {
      console.error("Error saving coupon:", err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteCoupon(id).unwrap();
      setDeleteConfirm(null);
    } catch (err) {
      console.error("Error deleting coupon:", err);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleString("vi-VN");
  };

  const formatCurrency = (amount) => {
    if (!amount) return "0";
    return new Intl.NumberFormat("vi-VN").format(amount);
  };

  const getDiscountDisplay = (coupon) => {
    if (coupon.discount_type === "PERCENTAGE") {
      return `${coupon.discount_value}%`;
    }
    return `${formatCurrency(coupon.discount_value)}đ`;
  };

  const isExpired = (coupon) => {
    if (!coupon.valid_to) return false;
    return new Date(coupon.valid_to) < new Date();
  };

  const isActive = (coupon) => {
    if (!coupon.is_active) return false;
    if (isExpired(coupon)) return false;
    if (coupon.usage_limit && coupon.used_count >= coupon.usage_limit)
      return false;
    return true;
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <FontAwesomeIcon
            icon={faSpinner}
            spin
            size="3x"
            className="text-indigo-600"
          />
          <p className="mt-4 text-gray-600">Đang tải dữ liệu...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center text-red-500">
          <FontAwesomeIcon icon={faExclamationTriangle} size="3x" />
          <p className="mt-4">Lỗi khi tải dữ liệu</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="container mx-auto px-4 py-8">
        <div className="mb-6 flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <Link to="/admin">
              <motion.button
                whileHover={{ x: -5 }}
                className="text-indigo-600 font-semibold flex items-center"
              >
                <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
                Quay lại
              </motion.button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold text-gray-800 flex items-center">
                <FontAwesomeIcon
                  icon={faTag}
                  className="mr-3 text-indigo-600"
                />
                Quản lý Coupon
              </h1>
              <p className="text-gray-600 mt-1">
                Quản lý mã giảm giá và khuyến mãi
              </p>
            </div>
          </div>
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => handleOpenModal()}
            className="bg-indigo-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition-colors flex items-center"
          >
            <FontAwesomeIcon icon={faPlus} className="mr-2" />
            Tạo Coupon Mới
          </motion.button>
        </div>

        <div className="bg-white rounded-xl shadow-lg overflow-hidden">
          {coupons.length === 0 ? (
            <div className="text-center py-12">
              <FontAwesomeIcon
                icon={faTag}
                className="text-6xl text-gray-300 mb-4"
              />
              <p className="text-gray-500 text-lg">Chưa có coupon nào</p>
              <button
                onClick={() => handleOpenModal()}
                className="mt-4 text-indigo-600 hover:text-indigo-700 font-semibold"
              >
                Tạo coupon đầu tiên
              </button>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      Mã Coupon
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      Giảm giá
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      Đơn tối thiểu
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      Thời gian
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      Sử dụng
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      Trạng thái
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      Thao tác
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {coupons.map((coupon, index) => (
                    <motion.tr
                      key={coupon.id}
                      initial={{ opacity: 0, y: 20 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: index * 0.05 }}
                      className="hover:bg-gray-50"
                    >
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <span className="font-mono font-bold text-indigo-600 text-lg">
                            {coupon.code}
                          </span>
                        </div>
                        {coupon.description && (
                          <p className="text-sm text-gray-500 mt-1">
                            {coupon.description}
                          </p>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm font-semibold text-gray-900">
                          {getDiscountDisplay(coupon)}
                        </div>
                        {coupon.max_discount_amount &&
                          coupon.discount_type === "PERCENTAGE" && (
                            <div className="text-xs text-gray-500">
                              Tối đa:{" "}
                              {formatCurrency(coupon.max_discount_amount)}đ
                            </div>
                          )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                        {coupon.min_order_value > 0
                          ? `${formatCurrency(coupon.min_order_value)}đ`
                          : "Không có"}
                      </td>
                      <td className="px-6 py-4">
                        <div className="text-sm text-gray-600">
                          <div>Từ: {formatDate(coupon.valid_from)}</div>
                          <div>Đến: {formatDate(coupon.valid_to)}</div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                        {coupon.used_count || 0} / {coupon.usage_limit || "∞"}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {isActive(coupon) ? (
                          <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-800">
                            <FontAwesomeIcon
                              icon={faCheckCircle}
                              className="mr-1"
                            />
                            Hoạt động
                          </span>
                        ) : (
                          <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-red-100 text-red-800">
                            <FontAwesomeIcon
                              icon={faTimesCircle}
                              className="mr-1"
                            />
                            {isExpired(coupon)
                              ? "Hết hạn"
                              : !coupon.is_active
                                ? "Tắt"
                                : "Hết lượt"}
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div className="flex space-x-2">
                          <button
                            onClick={() => handleOpenModal(coupon)}
                            className="text-indigo-600 hover:text-indigo-900 transition-colors"
                          >
                            <FontAwesomeIcon icon={faEdit} />
                          </button>
                          <button
                            onClick={() => setDeleteConfirm(coupon.id)}
                            className="text-red-600 hover:text-red-900 transition-colors"
                          >
                            <FontAwesomeIcon icon={faTrash} />
                          </button>
                        </div>
                      </td>
                    </motion.tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </main>

      {/* Delete Confirmation Modal */}
      <AnimatePresence>
        {deleteConfirm && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4"
            onClick={() => setDeleteConfirm(null)}
          >
            <motion.div
              initial={{ scale: 0.9 }}
              animate={{ scale: 1 }}
              exit={{ scale: 0.9 }}
              className="bg-white rounded-xl p-6 max-w-md w-full"
              onClick={(e) => e.stopPropagation()}
            >
              <h3 className="text-xl font-bold text-gray-800 mb-4">
                Xác nhận xóa
              </h3>
              <p className="text-gray-600 mb-6">
                Bạn có chắc chắn muốn xóa coupon này? Hành động này không thể
                hoàn tác.
              </p>
              <div className="flex justify-end space-x-4">
                <button
                  onClick={() => setDeleteConfirm(null)}
                  className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
                >
                  Hủy
                </button>
                <button
                  onClick={() => handleDelete(deleteConfirm)}
                  disabled={isDeleting}
                  className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50 flex items-center"
                >
                  {isDeleting ? (
                    <>
                      <FontAwesomeIcon icon={faSpinner} spin className="mr-2" />
                      Đang xóa...
                    </>
                  ) : (
                    "Xóa"
                  )}
                </button>
              </div>
            </motion.div>
          </motion.div>
        )}
      </AnimatePresence>

      {/* Form Modal */}
      {isModalOpen && (
        <CouponFormModal
          coupon={selectedCoupon}
          onClose={handleCloseModal}
          onSave={handleSave}
          isLoading={isCreating || isUpdating}
        />
      )}

      <Footer />
    </div>
  );
};

export default CouponAdminPage;
