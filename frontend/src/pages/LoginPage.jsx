import React, { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useDispatch } from "react-redux";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSignInAlt, faCheckCircle } from "@fortawesome/free-solid-svg-icons";
import InputField from "../components/ui/InputField";
import MinimalHeader from "../components/layout/MinimalHeader";
import { useLoginMutation } from "../app/authApi";
import { setCredentials } from "../app/authSlice";

const LoginPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const [login, { isLoading, error }] = useLoginMutation();
  const [successMessage, setSuccessMessage] = useState(null);

  const [formData, setFormData] = useState({
    email: "",
    password: "",
    rememberMe: false,
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (location.state?.message) {
      setSuccessMessage(location.state.message);
      // Xóa message sau 5 giây
      const timer = setTimeout(() => setSuccessMessage(null), 5000);
      return () => clearTimeout(timer);
    }
  }, [location.state]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
    // Xóa lỗi khi người dùng nhập
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.email) {
      newErrors.email = "Email là bắt buộc";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email không hợp lệ";
    }
    if (!formData.password) {
      newErrors.password = "Mật khẩu là bắt buộc";
    } else if (formData.password.length < 6) {
      newErrors.password = "Mật khẩu phải có ít nhất 6 ký tự";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const result = await login({
        email: formData.email,
        password: formData.password,
      }).unwrap();

      // Lưu thông tin đăng nhập vào Redux store
      dispatch(setCredentials(result));

      // Đợi một chút để đảm bảo Redux store được cập nhật
      await new Promise((resolve) => setTimeout(resolve, 100));

      // Chuyển hướng về trang chủ
      navigate("/");
    } catch (err) {
      // Lỗi đã được xử lý bởi RTK Query
      console.error("Login error:", err);
      if (err.data) {
        console.error("Error data:", err.data);
      }
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <MinimalHeader />
      <div className="flex-grow flex items-center justify-center">
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          className="w-full max-w-md p-8 space-y-8 bg-white rounded-2xl shadow-xl"
        >
          <div className="text-center">
            <h1 className="text-4xl font-bold text-gray-800 mb-2">Đăng nhập</h1>
            <p className="text-gray-600">Chào mừng trở lại với FashionHub!</p>
          </div>

          {successMessage && (
            <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg flex items-center">
              <FontAwesomeIcon icon={faCheckCircle} className="mr-2" />
              {successMessage}
            </div>
          )}

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
              {error.data?.message || "Email hoặc mật khẩu không đúng"}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <InputField
                label="Email"
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="example@email.com"
                error={errors.email}
              />
            </div>
            <div>
              <InputField
                label="Mật khẩu"
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="••••••••"
                error={errors.password}
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <input
                  id="remember-me"
                  name="rememberMe"
                  type="checkbox"
                  checked={formData.rememberMe}
                  onChange={handleChange}
                  className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                />
                <label
                  htmlFor="remember-me"
                  className="ml-2 block text-sm text-gray-900"
                >
                  Ghi nhớ đăng nhập
                </label>
              </div>

              <div className="text-sm">
                <a
                  href="#"
                  className="font-medium text-indigo-600 hover:text-indigo-500"
                >
                  Quên mật khẩu?
                </a>
              </div>
            </div>

            <motion.button
              type="submit"
              disabled={isLoading}
              whileHover={{ scale: isLoading ? 1 : 1.02 }}
              whileTap={{ scale: isLoading ? 1 : 0.98 }}
              className="w-full flex justify-center py-3 px-4 border border-transparent rounded-xl shadow-lg text-sm font-medium text-white bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? (
                "Đang đăng nhập..."
              ) : (
                <>
                  <FontAwesomeIcon icon={faSignInAlt} className="mr-2" />
                  Đăng nhập
                </>
              )}
            </motion.button>
          </form>

          <p className="text-center text-sm text-gray-600">
            Chưa có tài khoản?{" "}
            <Link
              to="/register"
              className="font-medium text-indigo-600 hover:text-indigo-500"
            >
              Đăng ký ngay
            </Link>
          </p>
        </motion.div>
      </div>
    </div>
  );
};

export default LoginPage;
