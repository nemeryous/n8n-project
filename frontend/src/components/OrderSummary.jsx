import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShoppingCart,
  faTruck,
  faShieldAlt,
} from "@fortawesome/free-solid-svg-icons";

const OrderSummary = ({ cartItems, shippingFee }) => {
  const subtotal = cartItems.reduce(
    (sum, item) => (item ? sum + item.unit_price * item.quantity : sum),
    0
  );
  const total = subtotal + shippingFee;

  const validItemsCount = cartItems.filter((item) => item).length;

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="bg-gradient-to-br from-indigo-50 to-purple-50 rounded-2xl p-6 sticky top-24"
    >
      <h3 className="text-2xl font-bold text-gray-800 mb-6 flex items-center">
        <FontAwesomeIcon
          icon={faShoppingCart}
          className="mr-3 text-indigo-600"
        />
        Đơn hàng của bạn
      </h3>

      <div className="space-y-4 mb-6">
        <div className="flex justify-between text-gray-600">
          <span>Tạm tính ({validItemsCount} sản phẩm)</span>
          <span className="font-semibold">
            {subtotal.toLocaleString("vi-VN")}đ
          </span>
        </div>

        <div className="flex justify-between text-gray-600">
          <span className="flex items-center">
            <FontAwesomeIcon icon={faTruck} className="mr-2 text-indigo-600" />
            Phí vận chuyển
          </span>
          <span className="font-semibold">
            {shippingFee.toLocaleString("vi-VN")}đ
          </span>
        </div>

        <div className="border-t-2 border-indigo-200 pt-4">
          <div className="flex justify-between items-center">
            <span className="text-xl font-bold text-gray-800">Tổng cộng</span>
            <motion.span
              key={total}
              initial={{ scale: 1.2 }}
              animate={{ scale: 1 }}
              className="text-3xl font-bold text-indigo-600"
            >
              {total.toLocaleString("vi-VN")}đ
            </motion.span>
          </div>
        </div>
      </div>

      <div className="bg-blue-50 border border-blue-200 rounded-xl p-4 text-sm text-blue-800">
        <FontAwesomeIcon icon={faShieldAlt} className="mr-2" />
        Đơn hàng của bạn được bảo vệ bởi chính sách hoàn tiền 100%
      </div>
    </motion.div>
  );
};

export default OrderSummary;
