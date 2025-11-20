import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShoppingCart,
  faUser,
  faSignOutAlt,
  faChevronDown,
} from "@fortawesome/free-solid-svg-icons";
import NavLink from "../ui/NavLink";
import { Link, useNavigate } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import { useSelector, useDispatch } from "react-redux";

import { useGetCartItemsByCustomerIdQuery } from "../../app/cartItemApi";
import { useGetOrCreateCartByCustomerQuery } from "../../app/cartApi";
import { logout } from "../../app/authSlice";
import { useLogoutMutation, useLogoutAllMutation } from "../../app/authApi";

const Header = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { user, isAuthenticated, refreshToken } = useSelector(
    (state) => state.auth,
  );
  const [logoutApi] = useLogoutMutation();
  const [logoutAllApi] = useLogoutAllMutation();
  const [showUserMenu, setShowUserMenu] = useState(false);

  const customerId = user?.id || null;
  const { data: cart } = useGetOrCreateCartByCustomerQuery(customerId, {
    skip: !customerId,
  });
  const { data: cartItems, isLoading } = useGetCartItemsByCustomerIdQuery(
    cart?.id,
    {
      skip: !cart || !customerId,
    },
  );

  const cartCount = isLoading
    ? 0
    : cartItems?.reduce((total, item) => total + item.quantity, 0) || 0;

  const handleLogout = async () => {
    try {
      // Gửi refreshToken để server có thể invalidate token
      await logoutApi(refreshToken).unwrap();
    } catch (error) {
      console.error("Logout error:", error);
    } finally {
      // Luôn xóa state local dù API có lỗi hay không
      dispatch(logout());
      navigate("/");
      setShowUserMenu(false);
    }
  };

  const handleLogoutAll = async () => {
    try {
      // Gửi refreshToken để server có thể invalidate tất cả tokens
      await logoutAllApi(refreshToken).unwrap();
    } catch (error) {
      console.error("Logout all error:", error);
    } finally {
      // Luôn xóa state local dù API có lỗi hay không
      dispatch(logout());
      navigate("/");
      setShowUserMenu(false);
    }
  };

  return (
    <header className="bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 text-white shadow-2xl sticky top-0 z-50">
      <div className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/">
            <motion.div
              whileHover={{ scale: 1.05 }}
              className="text-3xl font-bold tracking-tight"
            >
              <span className="bg-white text-transparent bg-clip-text">
                Fashion
              </span>
              <span className="text-yellow-300">Hub</span>
            </motion.div>
          </Link>

          <nav className="hidden md:flex space-x-8">
            <NavLink to="/">Trang chủ</NavLink>
            <NavLink to="/products">Sản phẩm</NavLink>
            <NavLink to="/about-me">Về chúng tôi</NavLink>
            {user?.role === "ADMIN" && (
              <NavLink to="/admin">Admin</NavLink>
            )}
          </nav>

          <div className="flex items-center space-x-4">
            <Link to="/checkout">
              <motion.div
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.95 }}
                className="relative cursor-pointer"
              >
                <FontAwesomeIcon icon={faShoppingCart} className="text-2xl" />
                {cartCount > 0 && (
                  <motion.span
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    className="absolute -top-2 -right-2 bg-red-500 text-white text-xs font-bold rounded-full w-6 h-6 flex items-center justify-center"
                  >
                    {cartCount}
                  </motion.span>
                )}
              </motion.div>
            </Link>

            {isAuthenticated && user ? (
              <div className="relative">
                <motion.button
                  onClick={() => setShowUserMenu(!showUserMenu)}
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className="flex items-center space-x-2 px-3 py-2 rounded-lg hover:bg-white/10 transition-colors"
                >
                  <FontAwesomeIcon icon={faUser} />
                  <span className="hidden md:inline">{user.name}</span>
                  <FontAwesomeIcon
                    icon={faChevronDown}
                    className={`text-xs transition-transform ${
                      showUserMenu ? "rotate-180" : ""
                    }`}
                  />
                </motion.button>

                <AnimatePresence>
                  {showUserMenu && (
                    <motion.div
                      initial={{ opacity: 0, y: -10 }}
                      animate={{ opacity: 1, y: 0 }}
                      exit={{ opacity: 0, y: -10 }}
                      className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-xl py-2 z-50"
                    >
                      <div className="px-4 py-2 border-b border-gray-200">
                        <p className="text-sm font-semibold text-gray-800">
                          {user.name}
                        </p>
                        <p className="text-xs text-gray-500">{user.email}</p>
                      </div>
                      <button
                        onClick={handleLogout}
                        className="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 flex items-center space-x-2"
                      >
                        <FontAwesomeIcon icon={faSignOutAlt} />
                        <span>Đăng xuất</span>
                      </button>
                      <button
                        onClick={handleLogoutAll}
                        className="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 flex items-center space-x-2 border-t border-gray-200"
                      >
                        <FontAwesomeIcon icon={faSignOutAlt} />
                        <span>Đăng xuất tất cả phiên</span>
                      </button>
                    </motion.div>
                  )}
                </AnimatePresence>
              </div>
            ) : (
              <Link to="/login">
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className="px-4 py-2 rounded-lg bg-white/20 hover:bg-white/30 transition-colors"
                >
                  Đăng nhập
                </motion.button>
              </Link>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
