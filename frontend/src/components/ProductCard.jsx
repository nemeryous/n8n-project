import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShoppingCart,
  faHeart,
  faStar,
} from "@fortawesome/free-solid-svg-icons";

const ProductCard = ({ product, onAddToCart }) => {
  const [isHovered, setIsHovered] = useState(false);
  const [isLiked, setIsLiked] = useState(false);
  const [showNotification, setShowNotification] = useState(false);

  const handleAddToCartClick = (e) => {
    e.preventDefault(); // Prevent navigation
    onAddToCart(product);
    setShowNotification(true);
    setTimeout(() => setShowNotification(false), 2000);
  };

  return (
    <motion.div
      layout
      initial={{ opacity: 0, y: 50 }}
      animate={{ opacity: 1, y: 0 }}
      whileHover={{ y: -10 }}
      onHoverStart={() => setIsHovered(true)}
      onHoverEnd={() => setIsHovered(false)}
      className="bg-white rounded-2xl overflow-hidden shadow-lg hover:shadow-2xl transition-all duration-300 relative group"
    >
      {/* Notification */}
      <AnimatePresence>
        {showNotification && (
          <motion.div
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            className="absolute top-4 left-4 right-4 bg-green-500 text-white px-4 py-2 rounded-lg shadow-lg z-20 text-center font-medium"
          >
            ✓ Đã thêm vào giỏ hàng!
          </motion.div>
        )}
      </AnimatePresence>

      {/* Image Container */}
      <div className="relative h-72 overflow-hidden bg-gray-100">
        <motion.img
          src={product.image}
          alt={product.name}
          animate={{ scale: isHovered ? 1.1 : 1 }}
          transition={{ duration: 0.4 }}
          className="w-full h-full object-cover"
        />

        {/* Overlay with actions */}
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: isHovered ? 1 : 0 }}
          className="absolute inset-0 bg-gradient-to-t from-black/60 via-black/20 to-transparent flex items-end justify-center pb-6"
        >
          <motion.button
            initial={{ y: 20 }}
            animate={{ y: isHovered ? 0 : 20 }}
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={handleAddToCartClick}
            className="bg-white text-gray-900 px-6 py-3 rounded-full font-semibold shadow-xl hover:bg-yellow-400 transition-colors"
          >
            <FontAwesomeIcon icon={faShoppingCart} className="mr-2" />
            Thêm vào giỏ
          </motion.button>
        </motion.div>

        {/* Wishlist button */}
        <motion.button
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          onClick={(e) => {
            e.preventDefault();
            setIsLiked(!isLiked);
          }}
          className="absolute top-4 right-4 bg-white w-10 h-10 rounded-full shadow-lg flex items-center justify-center z-10"
        >
          <FontAwesomeIcon
            icon={faHeart}
            className={`${
              isLiked ? "text-red-500" : "text-gray-400"
            } transition-colors`}
          />
        </motion.button>

        {/* Sale badge */}
        <div className="absolute top-4 left-4 bg-red-500 text-white px-3 py-1 rounded-full text-sm font-bold">
          -30%
        </div>
      </div>

      {/* Product Info */}
      <div className="p-6">
        <h3 className="text-lg font-bold text-gray-800 mb-2 line-clamp-2 h-14">
          {product.name}
        </h3>

        <div className="flex items-center mb-3">
          <div className="flex text-yellow-400 mr-2">
            {[...Array(5)].map((_, i) => (
              <FontAwesomeIcon
                key={i}
                icon={faStar}
                className={`text-sm ${
                  i < Math.floor(product.rating) ? "opacity-100" : "opacity-30"
                }`}
              />
            ))}
          </div>
          <span className="text-sm text-gray-600">
            {product.rating} ({product.sold} đã bán)
          </span>
        </div>

        <div className="flex items-center justify-between">
          <div>
            <span className="text-2xl font-bold text-indigo-600">
              {product.price.toLocaleString("vi-VN")}đ
            </span>
          </div>
          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={handleAddToCartClick}
            className="md:hidden bg-indigo-600 text-white w-10 h-10 rounded-full flex items-center justify-center shadow-lg"
          >
            <FontAwesomeIcon icon={faShoppingCart} />
          </motion.button>
        </div>
      </div>
    </motion.div>
  );
};

export default ProductCard;
