import React, { useState } from "react";
import { useParams, Link } from "react-router-dom";
import { motion } from "framer-motion";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShoppingCart,
  faStar,
  faArrowLeft,
  faCheckCircle,
  faShieldAlt,
} from "@fortawesome/free-solid-svg-icons";

// Mock data - In production, this would be fetched from an API
const mockProducts = [
  {
    id: 1,
    name: "Áo Thun Premium Cotton",
    price: 250000,
    image:
      "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800&h=800&fit=crop",
    rating: 4.5,
    sold: 1200,
    description:
      "Chất liệu 100% cotton cao cấp, thoáng mát, thấm hút mồ hôi tốt. Form áo regular fit, phù hợp với mọi vóc dáng. Đây là item không thể thiếu trong tủ đồ của bạn.",
    sizes: ["S", "M", "L", "XL"],
    colors: ["Trắng", "Đen", "Xanh Navy"],
  },
  // ... add other products here if needed
];

const ProductDetailPage = () => {
  const { id } = useParams();
  const product = mockProducts.find((p) => p.id === parseInt(id));
  const [cartCount, setCartCount] = useState(0);
  const [selectedSize, setSelectedSize] = useState(null);
  const [selectedColor, setSelectedColor] = useState(null);

  const handleAddToCart = () => {
    setCartCount(cartCount + 1);
  };

  if (!product) {
    return <div>Sản phẩm không tồn tại</div>;
  }

  return (
    <div className="min-h-screen bg-white">
      <Header cartCount={cartCount} />

      <main className="container mx-auto px-4 py-12">
        <Link to="/products">
          <motion.button
            whileHover={{ x: -5 }}
            className="text-indigo-600 font-semibold flex items-center mb-8 hover:text-indigo-700"
          >
            <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
            Tất cả sản phẩm
          </motion.button>
        </Link>

        <div className="grid md:grid-cols-2 gap-12">
          {/* Product Image */}
          <motion.div
            initial={{ opacity: 0, x: -50 }}
            animate={{ opacity: 1, x: 0 }}
            className="w-full h-full"
          >
            <img
              src={product.image}
              alt={product.name}
              className="w-full h-auto object-cover rounded-2xl shadow-2xl"
            />
          </motion.div>

          {/* Product Info */}
          <motion.div
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
          >
            <h1 className="text-4xl font-extrabold text-gray-800 mb-4">
              {product.name}
            </h1>

            <div className="flex items-center mb-4">
              <div className="flex text-yellow-400 mr-2">
                {[...Array(5)].map((_, i) => (
                  <FontAwesomeIcon
                    key={i}
                    icon={faStar}
                    className={`text-xl ${
                      i < Math.floor(product.rating)
                        ? "opacity-100"
                        : "opacity-30"
                    }`}
                  />
                ))}
              </div>
              <span className="text-lg text-gray-600">
                {product.rating} ({product.sold} đã bán)
              </span>
            </div>

            <p className="text-4xl font-bold text-indigo-600 mb-6">
              {product.price.toLocaleString("vi-VN")}đ
            </p>

            <p className="text-gray-700 leading-relaxed mb-6">
              {product.description}
            </p>

            {/* Sizes */}
            <div className="mb-6">
              <h3 className="font-semibold text-gray-800 mb-2">Kích cỡ:</h3>
              <div className="flex space-x-2">
                {product.sizes.map((size) => (
                  <motion.button
                    key={size}
                    whileHover={{ scale: 1.05 }}
                    onClick={() => setSelectedSize(size)}
                    className={`px-4 py-2 rounded-lg border-2 font-medium transition-colors ${
                      selectedSize === size
                        ? "bg-indigo-600 text-white border-indigo-600"
                        : "bg-white text-gray-700 border-gray-300 hover:border-indigo-500"
                    }`}
                  >
                    {size}
                  </motion.button>
                ))}
              </div>
            </div>

            {/* Colors */}
            <div className="mb-8">
              <h3 className="font-semibold text-gray-800 mb-2">Màu sắc:</h3>
              <div className="flex space-x-2">
                {product.colors.map((color) => (
                  <motion.button
                    key={color}
                    whileHover={{ scale: 1.05 }}
                    onClick={() => setSelectedColor(color)}
                    className={`px-4 py-2 rounded-lg border-2 font-medium transition-colors ${
                      selectedColor === color
                        ? "bg-indigo-600 text-white border-indigo-600"
                        : "bg-white text-gray-700 border-gray-300 hover:border-indigo-500"
                    }`}
                  >
                    {color}
                  </motion.button>
                ))}
              </div>
            </div>

            <motion.button
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              onClick={handleAddToCart}
              className="w-full bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-4 rounded-xl font-bold text-lg shadow-xl hover:shadow-2xl transition-all flex items-center justify-center"
            >
              <FontAwesomeIcon icon={faShoppingCart} className="mr-3" />
              Thêm vào giỏ hàng
            </motion.button>

            <div className="mt-6 bg-gray-50 border border-gray-200 rounded-xl p-4 text-sm text-gray-700 flex items-center">
              <FontAwesomeIcon
                icon={faShieldAlt}
                className="mr-3 text-indigo-600 text-2xl"
              />
              <span>
                Đơn hàng của bạn được bảo vệ bởi chính sách hoàn tiền 100% của
                FashionHub.
              </span>
            </div>
          </motion.div>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default ProductDetailPage;
