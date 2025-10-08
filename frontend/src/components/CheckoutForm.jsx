import React, { useState } from "react";
import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheckCircle } from "@fortawesome/free-solid-svg-icons";
import InputField from "./ui/InputField";

const CheckoutForm = ({ onSubmit, isProcessing }) => {
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
    address: "",
    notes: "",
  });

  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};

    if (!formData.fullName.trim()) {
      newErrors.fullName = "Vui lòng nhập họ tên";
    }

    if (!formData.email.trim()) {
      newErrors.email = "Vui lòng nhập email";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email không hợp lệ";
    }

    if (!formData.phone.trim()) {
      newErrors.phone = "Vui lòng nhập số điện thoại";
    } else if (!/^[0-9]{10,11}$/.test(formData.phone.replace(/\s/g, ""))) {
      newErrors.phone = "Số điện thoại không hợp lệ";
    }

    if (!formData.address.trim()) {
      newErrors.address = "Vui lòng nhập địa chỉ giao hàng";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (validateForm()) {
      onSubmit(formData);
    }
  };

  const handleChange = (field) => (e) => {
    setFormData({ ...formData, [field]: e.target.value });
    if (errors[field]) {
      setErrors({ ...errors, [field]: "" });
    }
  };

  return (
    <motion.form
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      onSubmit={handleSubmit}
      className="bg-white rounded-2xl p-8 shadow-lg"
    >
      <h3 className="text-2xl font-bold text-gray-800 mb-6">
        Thông tin giao hàng
      </h3>

      <InputField
        label="Họ và tên"
        value={formData.fullName}
        onChange={handleChange("fullName")}
        placeholder="Nguyễn Văn A"
        error={errors.fullName}
      />

      <InputField
        label="Email"
        type="email"
        value={formData.email}
        onChange={handleChange("email")}
        placeholder="example@email.com"
        error={errors.email}
      />

      <InputField
        label="Số điện thoại"
        type="tel"
        value={formData.phone}
        onChange={handleChange("phone")}
        placeholder="0901234567"
        error={errors.phone}
      />

      <InputField
        label="Địa chỉ giao hàng"
        value={formData.address}
        onChange={handleChange("address")}
        placeholder="Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố"
        error={errors.address}
      />

      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-6"
      >
        <label className="block text-gray-700 font-semibold mb-2">
          Ghi chú
        </label>
        <textarea
          value={formData.notes}
          onChange={handleChange("notes")}
          placeholder="Ghi chú thêm về đơn hàng (không bắt buộc)"
          rows="4"
          className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:border-indigo-500 focus:outline-none transition-colors bg-white resize-none"
        />
      </motion.div>

      <motion.button
        type="submit"
        disabled={isProcessing}
        whileHover={{ scale: isProcessing ? 1 : 1.02 }}
        whileTap={{ scale: isProcessing ? 1 : 0.98 }}
        className={`w-full py-4 rounded-xl font-bold text-lg shadow-xl transition-all ${
          isProcessing
            ? "bg-gray-400 cursor-not-allowed"
            : "bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white"
        }`}
      >
        {isProcessing ? (
          <span className="flex items-center justify-center">
            <motion.div
              animate={{ rotate: 360 }}
              transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
              className="w-6 h-6 border-3 border-white border-t-transparent rounded-full mr-3"
            />
            Đang xử lý...
          </span>
        ) : (
          <span className="flex items-center justify-center">
            <FontAwesomeIcon icon={faCheckCircle} className="mr-2" />
            HOÀN TẤT ĐẶT HÀNG
          </span>
        )}
      </motion.button>
    </motion.form>
  );
};

export default CheckoutForm;
