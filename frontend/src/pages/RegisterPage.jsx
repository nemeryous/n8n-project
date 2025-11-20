import { useState } from "react";
import { motion } from "framer-motion";
import { Link, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserPlus } from "@fortawesome/free-solid-svg-icons";
import InputField from "../components/ui/InputField";
import MinimalHeader from "../components/layout/MinimalHeader";
import { useRegisterMutation } from "../app/authApi";
import { setCredentials } from "../app/authSlice";

const RegisterPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [register, { isLoading, error }] = useRegisterMutation();

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    phone: "",
    address: "",
  });

  const [errors, setErrors] = useState({});
  const [apiErrors, setApiErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Xóa lỗi khi người dùng nhập
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
    // Xóa lỗi API khi người dùng nhập
    if (apiErrors[name]) {
      setApiErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
    // Xóa lỗi chung nếu có
    if (apiErrors.general) {
      setApiErrors((prev) => {
        const { general, ...rest } = prev;
        return rest;
      });
    }
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.name.trim()) {
      newErrors.name = "Họ và tên là bắt buộc";
    }
    if (!formData.email) {
      newErrors.email = "Email là bắt buộc";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email không hợp lệ";
    }
    if (!formData.phone) {
      newErrors.phone = "Số điện thoại là bắt buộc";
    } else if (!/^[0-9]{10,11}$/.test(formData.phone.replace(/\s/g, ""))) {
      newErrors.phone = "Số điện thoại không hợp lệ";
    }
    if (!formData.address.trim()) {
      newErrors.address = "Địa chỉ là bắt buộc";
    }
    if (!formData.password) {
      newErrors.password = "Mật khẩu là bắt buộc";
    } else if (formData.password.length < 6) {
      newErrors.password = "Mật khẩu phải có ít nhất 6 ký tự";
    }
    if (!formData.confirmPassword) {
      newErrors.confirmPassword = "Vui lòng xác nhận mật khẩu";
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Mật khẩu xác nhận không khớp";
    }
    setErrors(newErrors);
    setApiErrors({}); // Xóa lỗi từ API khi validate lại
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiErrors({}); // Xóa lỗi API trước khi submit
    if (!validate()) return;

    try {
      const result = await register({
        name: formData.name,
        email: formData.email,
        password: formData.password,
        phone: formData.phone,
        address: formData.address,
      }).unwrap();

      // Nếu đăng ký thành công và API trả về token, tự động đăng nhập
      if (result.accessToken) {
        dispatch(setCredentials(result));
        navigate("/");
      } else {
        // Nếu không có token, chuyển đến trang đăng nhập
        navigate("/login", {
          state: { message: "Đăng ký thành công! Vui lòng đăng nhập." },
        });
      }
    } catch (err) {
      // Xử lý lỗi từ API
      if (err.data) {
        const errorData = err.data;
        
        // Xử lý validation errors từ server
        if (errorData.validation_errors && Array.isArray(errorData.validation_errors)) {
          const validationErrors = {};
          errorData.validation_errors.forEach((error) => {
            validationErrors[error.field] = error.message;
          });
          setApiErrors(validationErrors);
        } else {
          // Hiển thị thông báo lỗi chung
          setApiErrors({ general: errorData.message || "Đăng ký thất bại. Vui lòng thử lại." });
        }
      } else {
        setApiErrors({ general: "Đăng ký thất bại. Vui lòng thử lại." });
      }
      console.error("Register error:", err);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <MinimalHeader />
      <div className="flex-grow flex items-center justify-center py-12">
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          className="w-full max-w-md p-8 space-y-8 bg-white rounded-2xl shadow-xl"
        >
          <div className="text-center">
            <h1 className="text-4xl font-bold text-gray-800 mb-2">
              Tạo tài khoản
            </h1>
            <p className="text-gray-600">
              Gia nhập cộng đồng FashionHub ngay hôm nay!
            </p>
          </div>

          {apiErrors.general && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
              {apiErrors.general}
            </div>
          )}

          {error && !apiErrors.general && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
              {error.data?.message || "Đăng ký thất bại. Vui lòng thử lại."}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <InputField
              label="Họ và tên"
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Nguyễn Văn A"
              error={errors.name || apiErrors.name}
              required
            />
            <InputField
              label="Email"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="example@email.com"
              error={errors.email || apiErrors.email}
              required
            />
            <InputField
              label="Số điện thoại"
              type="tel"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              placeholder="0123456789"
              error={errors.phone || apiErrors.phone}
              required
            />
            <InputField
              label="Địa chỉ"
              type="text"
              name="address"
              value={formData.address}
              onChange={handleChange}
              placeholder="123 Đường ABC, Quận XYZ"
              error={errors.address || apiErrors.address}
              required
            />
            <InputField
              label="Mật khẩu"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="••••••••"
              error={errors.password || apiErrors.password}
              required
            />
            <InputField
              label="Xác nhận mật khẩu"
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="••••••••"
              error={errors.confirmPassword}
              required
            />

            <motion.button
              type="submit"
              disabled={isLoading}
              whileHover={{ scale: isLoading ? 1 : 1.02 }}
              whileTap={{ scale: isLoading ? 1 : 0.98 }}
              className="w-full flex justify-center py-3 px-4 border border-transparent rounded-xl shadow-lg text-sm font-medium text-white bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? (
                "Đang đăng ký..."
              ) : (
                <>
                  <FontAwesomeIcon icon={faUserPlus} className="mr-2" />
                  Đăng ký
                </>
              )}
            </motion.button>
          </form>

          <p className="text-center text-sm text-gray-600">
            Đã có tài khoản?{" "}
            <Link
              to="/login"
              className="font-medium text-indigo-600 hover:text-indigo-500"
            >
              Đăng nhập
            </Link>
          </p>
        </motion.div>
      </div>
    </div>
  );
};

export default RegisterPage;
