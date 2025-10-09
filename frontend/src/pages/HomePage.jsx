import React, { useState } from "react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import Header from "../components/layout/Header";
import HeroBanner from "../components/HeroBanner";
import FeaturesSection from "../components/FeaturesSection";
import ProductCard from "../components/ProductCard";
import Footer from "../components/layout/Footer";
import { useGetProductsQuery } from "../app/productApi";
import { useCreateCartItemMutation } from "../app/cartItemApi";
import { useGetOrCreateCartByCustomerQuery } from "../app/cartApi";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faSpinner,
  faExclamationTriangle,
} from "@fortawesome/free-solid-svg-icons";

export default function HomePage() {
  const {
    data: productData,
    error,
    isLoading,
  } = useGetProductsQuery({ size: 6 }); // Lấy 6 sản phẩm nổi bật
  const [createCartItem] = useCreateCartItemMutation();
  const customerId = 1; // Giả sử, sẽ lấy từ state auth
  const { data: cart } = useGetOrCreateCartByCustomerQuery(customerId);

  const handleAddToCart = async (product) => {
    if (!cart) {
      console.error("Cart not available yet.");
      // Optionally, show a message to the user
      return;
    }
    try {
      // Giả sử API cart-items giờ đây cần cartId
      await createCartItem({
        product_id: product.id,
        customer_id: customerId,
        quantity: 1,
        cart_id: cart.id,
      }).unwrap();
      // Optionally, show a success notification
    } catch (err) {
      console.error("Failed to add item to cart: ", err);
      // Optionally, show an error notification
    }
  };

  const renderFeaturedProducts = () => {
    if (isLoading) {
      return (
        <div className="text-center text-gray-500 py-16">
          <FontAwesomeIcon icon={faSpinner} spin size="2x" />
          <p className="mt-3">Đang tải sản phẩm...</p>
        </div>
      );
    }

    if (error) {
      return (
        <div className="text-center text-red-500 py-16">
          <FontAwesomeIcon icon={faExclamationTriangle} size="2x" />
          <p className="mt-3">Lỗi khi tải sản phẩm.</p>
        </div>
      );
    }

    if (productData && productData.content.length > 0) {
      return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {productData.content.map((product, index) => (
            <motion.div
              key={product.id}
              initial={{ opacity: 0, y: 50 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              {/* Link is removed from wrapping ProductCard to prevent navigation when clicking add to cart */}
              <ProductCard
                product={product}
                onAddToCart={() => handleAddToCart(product)}
              />
            </motion.div>
          ))}
        </div>
      );
    }

    return (
      <div className="text-center text-gray-500 py-16">
        <p>Không có sản phẩm nổi bật nào.</p>
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-indigo-50">
      <Header />

      <HeroBanner />

      <main className="container mx-auto px-4 py-12">
        <FeaturesSection />

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.5 }}
        >
          <div className="flex items-center justify-between mb-8">
            <div>
              <h2 className="text-4xl font-bold text-gray-800 mb-2">
                Sản phẩm nổi bật
              </h2>
              <p className="text-gray-600">
                Khám phá những sản phẩm được yêu thích nhất
              </p>
            </div>
            <Link to="/products">
              <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                className="hidden md:block text-indigo-600 font-semibold hover:text-indigo-700 transition-colors"
              >
                Xem tất cả →
              </motion.button>
            </Link>
          </div>

          {renderFeaturedProducts()}
        </motion.div>
      </main>

      <Footer />
    </div>
  );
}
