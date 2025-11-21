import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes, faSpinner } from "@fortawesome/free-solid-svg-icons";
import InputField from "../ui/InputField";

const DISCOUNT_TYPES = [
  { value: "PERCENTAGE", label: "Phần trăm (%)" },
  { value: "FIXED_AMOUNT", label: "Số tiền cố định (VNĐ)" },
];

const APPLICABLE_TO = [
  { value: "ALL", label: "Tất cả sản phẩm" },
  { value: "SPECIFIC_PRODUCTS", label: "Sản phẩm cụ thể" },
  { value: "SPECIFIC_CATEGORIES", label: "Danh mục cụ thể" },
];

const CUSTOMER_SEGMENTS = [
  { value: "NEW", label: "Khách hàng mới" },
  { value: "REGULAR", label: "Khách hàng thường xuyên" },
  { value: "VIP", label: "Khách hàng VIP" },
  { value: "PRICE_SENSITIVE", label: "Khách hàng có độ nhạy giá cao" },
  { value: "LAPSED", label: "Khách hàng đã mua hàng" },
  { value: "ALL", label: "Tất cả khách hàng" },
];

const CouponFormModal = ({ coupon, onClose, onSave, isLoading }) => {
  const [formData, setFormData] = useState({
    code: coupon?.code || "",
    discount_type: coupon?.discount_type || "PERCENTAGE",
    discount_value: coupon?.discount_value || 0,
    min_order_value: coupon?.min_order_value || 0,
    max_discount_amount: coupon?.max_discount_amount || 0,
    valid_from: coupon?.valid_from
      ? new Date(coupon.valid_from).toISOString().slice(0, 16)
      : "",
    valid_to: coupon?.valid_to
      ? new Date(coupon.valid_to).toISOString().slice(0, 16)
      : "",
    usage_limit: coupon?.usage_limit || 1,
    applicable_to: coupon?.applicable_to || "ALL",
    product_ids: coupon?.product_ids?.join(",") || "",
    category_names: coupon?.category_names?.join(",") || "",
    customer_segment: coupon?.customer_segment || "ALL",
    description: coupon?.description || "",
    is_active: coupon?.is_active ?? true,
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (coupon) {
      setFormData({
        code: coupon.code || "",
        discount_type: coupon.discount_type || "PERCENTAGE",
        discount_value: coupon.discount_value || 0,
        min_order_value: coupon.min_order_value || 0,
        max_discount_amount: coupon.max_discount_amount || 0,
        valid_from: coupon.valid_from
          ? new Date(coupon.valid_from).toISOString().slice(0, 16)
          : "",
        valid_to: coupon.valid_to
          ? new Date(coupon.valid_to).toISOString().slice(0, 16)
          : "",
        usage_limit: coupon.usage_limit || 1,
        applicable_to: coupon.applicable_to || "ALL",
        product_ids: coupon.product_ids?.join(",") || "",
        category_names: coupon.category_names?.join(",") || "",
        customer_segment: coupon.customer_segment || "ALL",
        description: coupon.description || "",
        is_active: coupon.is_active ?? true,
      });
    }
  }, [coupon]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
    // Clear error when user types
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.code.trim()) {
      newErrors.code = "Mã coupon không được để trống";
    }
    if (!formData.discount_value || formData.discount_value <= 0) {
      newErrors.discount_value = "Giá trị giảm giá phải lớn hơn 0";
    }
    if (!formData.valid_from) {
      newErrors.valid_from = "Ngày bắt đầu không được để trống";
    }
    if (!formData.valid_to) {
      newErrors.valid_to = "Ngày kết thúc không được để trống";
    }
    if (formData.valid_from && formData.valid_to) {
      if (new Date(formData.valid_from) >= new Date(formData.valid_to)) {
        newErrors.valid_to = "Ngày kết thúc phải sau ngày bắt đầu";
      }
    }
    if (!formData.usage_limit || formData.usage_limit < 1) {
      newErrors.usage_limit = "Giới hạn sử dụng phải >= 1";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;

    // Prepare data for API
    const submitData = {
      ...formData,
      discount_value: parseFloat(formData.discount_value),
      min_order_value: parseFloat(formData.min_order_value) || 0,
      max_discount_amount: parseFloat(formData.max_discount_amount) || 0,
      usage_limit: parseInt(formData.usage_limit, 10),
      valid_from: new Date(formData.valid_from).toISOString(),
      valid_to: new Date(formData.valid_to).toISOString(),
      product_ids:
        formData.applicable_to === "SPECIFIC_PRODUCTS" && formData.product_ids
          ? formData.product_ids
              .split(",")
              .map((id) => parseInt(id.trim(), 10))
              .filter((id) => !isNaN(id))
          : null,
      category_names:
        formData.applicable_to === "SPECIFIC_CATEGORIES" &&
        formData.category_names
          ? formData.category_names
              .split(",")
              .map((cat) => cat.trim())
              .filter((cat) => cat.length > 0)
          : null,
    };

    // Remove null/empty fields
    Object.keys(submitData).forEach((key) => {
      if (submitData[key] === null || submitData[key] === "") {
        delete submitData[key];
      }
    });

    onSave(submitData);
  };

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4"
        onClick={onClose}
      >
        <motion.div
          initial={{ scale: 0.9, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          exit={{ scale: 0.9, opacity: 0 }}
          className="bg-white rounded-2xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto"
          onClick={(e) => e.stopPropagation()}
        >
          <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
            <h2 className="text-2xl font-bold text-gray-800">
              {coupon ? "Cập nhật Coupon" : "Tạo Coupon Mới"}
            </h2>
            <button
              onClick={onClose}
              className="text-gray-500 hover:text-gray-700 transition-colors"
            >
              <FontAwesomeIcon icon={faTimes} size="lg" />
            </button>
          </div>

          <form onSubmit={handleSubmit} className="p-6 space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <InputField
                label="Mã Coupon *"
                type="text"
                name="code"
                value={formData.code}
                onChange={handleChange}
                placeholder="SUMMER2025"
                error={errors.code}
                required
              />

              <div>
                <label className="block text-gray-700 font-semibold mb-2">
                  Loại giảm giá *
                </label>
                <select
                  name="discount_type"
                  value={formData.discount_type}
                  onChange={handleChange}
                  className="w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  {DISCOUNT_TYPES.map((type) => (
                    <option key={type.value} value={type.value}>
                      {type.label}
                    </option>
                  ))}
                </select>
              </div>

              <InputField
                label={`Giá trị giảm giá * (${
                  formData.discount_type === "PERCENTAGE" ? "%" : "VNĐ"
                })`}
                type="number"
                name="discount_value"
                value={formData.discount_value}
                onChange={handleChange}
                placeholder="10"
                error={errors.discount_value}
                required
                min="0.01"
                step="0.01"
              />

              <InputField
                label="Giá trị đơn hàng tối thiểu (VNĐ)"
                type="number"
                name="min_order_value"
                value={formData.min_order_value}
                onChange={handleChange}
                placeholder="0"
                min="0"
                step="1000"
              />

              {(formData.discount_type === "PERCENTAGE" ||
                formData.discount_type === "FIXED_AMOUNT") && (
                <InputField
                  label="Số tiền giảm tối đa (VNĐ)"
                  type="number"
                  name="max_discount_amount"
                  value={formData.max_discount_amount}
                  onChange={handleChange}
                  placeholder="0"
                  min="0"
                  step="1000"
                />
              )}

              <InputField
                label="Ngày bắt đầu *"
                type="datetime-local"
                name="valid_from"
                value={formData.valid_from}
                onChange={handleChange}
                error={errors.valid_from}
                required
              />

              <InputField
                label="Ngày kết thúc *"
                type="datetime-local"
                name="valid_to"
                value={formData.valid_to}
                onChange={handleChange}
                error={errors.valid_to}
                required
              />

              <InputField
                label="Giới hạn sử dụng *"
                type="number"
                name="usage_limit"
                value={formData.usage_limit}
                onChange={handleChange}
                placeholder="100"
                error={errors.usage_limit}
                required
                min="1"
              />

              <div>
                <label className="block text-gray-700 font-semibold mb-2">
                  Áp dụng cho
                </label>
                <select
                  name="applicable_to"
                  value={formData.applicable_to}
                  onChange={handleChange}
                  className="w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  {APPLICABLE_TO.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              </div>

              {formData.applicable_to === "SPECIFIC_PRODUCTS" && (
                <InputField
                  label="ID Sản phẩm (phân cách bằng dấu phẩy)"
                  type="text"
                  name="product_ids"
                  value={formData.product_ids}
                  onChange={handleChange}
                  placeholder="1, 2, 3"
                />
              )}

              {formData.applicable_to === "SPECIFIC_CATEGORIES" && (
                <InputField
                  label="Danh mục (phân cách bằng dấu phẩy)"
                  type="text"
                  name="category_names"
                  value={formData.category_names}
                  onChange={handleChange}
                  placeholder="Áo, Quần, Giày"
                />
              )}

              <div>
                <label className="block text-gray-700 font-semibold mb-2">
                  Phân khúc khách hàng
                </label>
                <select
                  name="customer_segment"
                  value={formData.customer_segment}
                  onChange={handleChange}
                  className="w-full px-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  {CUSTOMER_SEGMENTS.map((segment) => (
                    <option key={segment.value} value={segment.value}>
                      {segment.label}
                    </option>
                  ))}
                </select>
              </div>

              <div className="md:col-span-2">
                <InputField
                  label="Mô tả"
                  type="textarea"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="Mô tả về coupon..."
                  rows="3"
                />
              </div>

              {coupon && (
                <div className="md:col-span-2 flex items-center">
                  <input
                    type="checkbox"
                    id="is_active"
                    name="is_active"
                    checked={formData.is_active}
                    onChange={handleChange}
                    className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                  <label htmlFor="is_active" className="ml-2 text-gray-700">
                    Kích hoạt coupon
                  </label>
                </div>
              )}
            </div>

            <div className="flex justify-end space-x-4 pt-4 border-t border-gray-200">
              <button
                type="button"
                onClick={onClose}
                className="px-6 py-2 border-2 border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
              >
                Hủy
              </button>
              <button
                type="submit"
                disabled={isLoading}
                className="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
              >
                {isLoading ? (
                  <>
                    <FontAwesomeIcon icon={faSpinner} spin className="mr-2" />
                    Đang lưu...
                  </>
                ) : coupon ? (
                  "Cập nhật"
                ) : (
                  "Tạo mới"
                )}
              </button>
            </div>
          </form>
        </motion.div>
      </motion.div>
    </AnimatePresence>
  );
};

export default CouponFormModal;
