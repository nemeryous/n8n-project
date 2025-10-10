import React from "react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserPlus } from "@fortawesome/free-solid-svg-icons";
import InputField from "../components/ui/InputField";
import MinimalHeader from "../components/layout/MinimalHeader";

const RegisterPage = () => {
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

          <form className="space-y-6">
            <InputField
              label="Họ và tên"
              type="text"
              placeholder="Nguyễn Văn A"
            />
            <InputField
              label="Email"
              type="email"
              placeholder="example@email.com"
            />
            <InputField
              label="Mật khẩu"
              type="password"
              placeholder="••••••••"
            />
            <InputField
              label="Xác nhận mật khẩu"
              type="password"
              placeholder="••••••••"
            />

            <motion.button
              type="submit"
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              className="w-full flex justify-center py-3 px-4 border border-transparent rounded-xl shadow-lg text-sm font-medium text-white bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700"
            >
              <FontAwesomeIcon icon={faUserPlus} className="mr-2" />
              Đăng ký
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
