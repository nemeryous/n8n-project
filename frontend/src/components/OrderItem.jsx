import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBox } from "@fortawesome/free-solid-svg-icons";
import { motion } from "framer-motion";

const OrderItem = ({ item, index }) => {
  const totalPrice = item.price * item.quantity;

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: index * 0.1 }}
      className="flex items-center justify-between py-4 border-b border-gray-200 last:border-0"
    >
      <div className="flex items-center space-x-4">
        <div className="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center">
          <FontAwesomeIcon icon={faBox} className="text-indigo-600" />
        </div>
        <div>
          <h4 className="font-semibold text-gray-800">{item.name}</h4>
          <p className="text-sm text-gray-600">Số lượng: {item.quantity}</p>
        </div>
      </div>
      <div className="text-right">
        <p className="font-bold text-gray-800">
          {totalPrice.toLocaleString("vi-VN")}đ
        </p>
        <p className="text-sm text-gray-600">
          {item.price.toLocaleString("vi-VN")}đ/sp
        </p>
      </div>
    </motion.div>
  );
};

export default OrderItem;
