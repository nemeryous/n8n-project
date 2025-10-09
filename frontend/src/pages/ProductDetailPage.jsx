import React, { useState } from "react";
import { useParams, Link } from "react-router-dom";
import { motion } from "framer-motion";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";
import ProductReview from "../components/ProductReview";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowLeft,
  faExclamationTriangle,
  faShieldAlt,
  faShoppingCart,
  faSpinner,
} from "@fortawesome/free-solid-svg-icons";
import { useGetProductByIdQuery } from "../app/productApi";
import { useCreateCartItemMutation } from "../app/cartItemApi";
import { useGetOrCreateCartByCustomerQuery } from "../app/cartApi";

const ProductDetailPage = () => {
  const { id } = useParams();
  const { data: product, error, isLoading } = useGetProductByIdQuery(id);
  const [createCartItem] = useCreateCartItemMutation();
  const [selectedSize, setSelectedSize] = useState(null);
  const [selectedColor, setSelectedColor] = useState(null);
  const customerId = 1; // Giả sử, sẽ lấy từ state auth
  const { data: cart } = useGetOrCreateCartByCustomerQuery(customerId);

  const handleAddToCart = async () => {
    if (!cart || !product) {
      console.error("Cart or Product not available yet.");
      return;
    }
    try {
      await createCartItem({
        product_id: product.id,
        customer_id: customerId,
        quantity: 1,
      }).unwrap();
    } catch (err) {
      console.error("Failed to add item to cart: ", err);
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="text-center text-gray-500">
          <FontAwesomeIcon icon={faSpinner} spin size="3x" />
          <p className="mt-4 text-lg">Đang tải chi tiết sản phẩm...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="text-center text-red-500">
          <FontAwesomeIcon icon={faExclamationTriangle} size="3x" />
          <p className="mt-4 text-lg">
            Không thể tải thông tin sản phẩm. Vui lòng thử lại.
          </p>
        </div>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="text-center text-gray-500">
          <h1 className="text-2xl">Sản phẩm không tồn tại</h1>
          <Link
            to="/products"
            className="text-indigo-600 hover:underline mt-4 inline-block"
          >
            Quay lại trang sản phẩm
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-white">
      <Header />

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
              src={product.image_url || "https://via.placeholder.com/800x800"}
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
              <span className="text-lg text-gray-600">
                Số lượng còn lại: {product.stock_quantity}
              </span>
            </div>

            <p className="text-4xl font-bold text-indigo-600 mb-6">
              {product.price.toLocaleString("vi-VN")}đ
            </p>

            <p className="text-gray-700 leading-relaxed mb-6">
              {product.description}
            </p>

            {/* Sizes */}
            {product.sizes && product.sizes.length > 0 && (
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
            )}

            {/* Colors */}
            {product.colors && product.colors.length > 0 && (
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
            )}

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

        <ProductReview />
      </main>

      <Footer />
    </div>
  );
};

export default ProductDetailPage;
