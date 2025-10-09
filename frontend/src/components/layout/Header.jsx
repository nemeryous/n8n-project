import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faShoppingCart } from "@fortawesome/free-solid-svg-icons";
import NavLink from "../ui/NavLink";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

import { useGetCartItemsByCustomerIdQuery } from "../../app/cartItemApi";
import { useGetOrCreateCartByCustomerQuery } from "../../app/cartApi";

const Header = () => {
  const customerId = 1; // Giả sử, sẽ lấy từ state auth
  const { data: cart } = useGetOrCreateCartByCustomerQuery(customerId);
  const { data: cartItems, isLoading } = useGetCartItemsByCustomerIdQuery(
    cart?.id,
    {
      skip: !cart, // Bỏ qua query này nếu chưa có cart
    }
  );

  const cartCount = isLoading
    ? 0
    : cartItems?.reduce((total, item) => total + item.quantity, 0) || 0;

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
            <NavLink to="/admin">Admin</NavLink>
            <NavLink to="/login">Đăng nhập</NavLink>
          </nav>

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
        </div>
      </div>
    </header>
  );
};

export default Header;
