import React, { useState } from "react";
import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";

const CartItem = ({ item, onUpdateQuantity, onRemove }) => {
  const [isRemoving, setIsRemoving] = useState(false);

  // Defensive check for item and product
  if (!item) {
    // Render a placeholder or loader if product data is not yet available
    return (
      <div className="bg-white rounded-2xl p-4 shadow-md animate-pulse">
        <div className="flex items-center space-x-4">
          <div className="w-24 h-24 rounded-xl bg-gray-200"></div>
          <div className="flex-1 space-y-2">
            <div className="h-4 bg-gray-200 rounded w-3/4"></div>
            <div className="h-6 bg-gray-200 rounded w-1/2"></div>
          </div>
        </div>
      </div>
    );
  }

  const handleRemove = () => {
    setIsRemoving(true);
    setTimeout(() => onRemove(item.id), 300);
  };

  return (
    <motion.div
      layout
      initial={{ opacity: 0, x: -50 }}
      animate={{
        opacity: isRemoving ? 0 : 1,
        x: isRemoving ? -100 : 0,
      }}
      exit={{ opacity: 0, x: -100 }}
      className="bg-white rounded-2xl p-4 shadow-md hover:shadow-xl transition-all mb-4"
    >
      <div className="flex items-center space-x-4">
        <motion.div
          whileHover={{ scale: 1.05 }}
          className="w-24 h-24 rounded-xl overflow-hidden flex-shrink-0 bg-gray-100"
        >
          <img
            src={item.image_url}
            alt={item.product_name}
            className="w-full h-full object-cover"
          />
        </motion.div>

        <div className="flex-1 min-w-0">
          <h3 className="font-bold text-gray-800 text-lg mb-1 truncate">
            {item.product_name}
          </h3>
          <p className="text-indigo-600 font-semibold text-xl">
            {item.unit_price.toLocaleString("vi-VN")}đ
          </p>
        </div>

        <div className="flex items-center space-x-3">
          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={() => onUpdateQuantity(item.id, item.quantity - 1)}
            disabled={item.quantity <= 1}
            className={`w-8 h-8 rounded-full flex items-center justify-center ${
              item.quantity <= 1
                ? "bg-gray-200 text-gray-400 cursor-not-allowed"
                : "bg-indigo-100 text-indigo-600 hover:bg-indigo-200"
            } transition-colors`}
          >
            <FontAwesomeIcon icon={faMinus} className="text-sm" />
          </motion.button>

          <motion.span
            key={item.quantity}
            initial={{ scale: 1.3 }}
            animate={{ scale: 1 }}
            className="font-bold text-gray-800 w-8 text-center"
          >
            {item.quantity}
          </motion.span>

          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={() => onUpdateQuantity(item.id, item.quantity + 1)}
            className="w-8 h-8 rounded-full bg-indigo-100 text-indigo-600 hover:bg-indigo-200 flex items-center justify-center transition-colors"
          >
            <FontAwesomeIcon icon={faPlus} className="text-sm" />
          </motion.button>
        </div>

        <motion.button
          whileHover={{ scale: 1.1, rotate: 10 }}
          whileTap={{ scale: 0.9 }}
          onClick={handleRemove}
          className="w-10 h-10 rounded-full bg-red-50 text-red-500 hover:bg-red-100 flex items-center justify-center transition-colors ml-2"
        >
          <FontAwesomeIcon icon={faTrash} />
        </motion.button>
      </div>
    </motion.div>
  );
};

export default CartItem;
