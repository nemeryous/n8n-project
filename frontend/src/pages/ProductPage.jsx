import React, { useState } from "react";
import { motion } from "framer-motion";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";
import ProductCard from "../components/ProductCard";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFilter,
  faSort,
  faSpinner,
  faExclamationTriangle,
} from "@fortawesome/free-solid-svg-icons";
import { useGetProductsQuery } from "../app/productApi";
import { useCreateCartItemMutation } from "../app/cartItemApi";
import { useGetOrCreateCartByCustomerQuery } from "../app/cartApi";
import { Link } from "react-router-dom";

const ProductPage = () => {
  const [search, setSearch] = useState("");
  const {
    data: productData,
    error,
    isLoading,
  } = useGetProductsQuery({ search });
  const [createCartItem] = useCreateCartItemMutation();
  const customerId = 1; // Giả sử, sẽ lấy từ state auth
  const { data: cart } = useGetOrCreateCartByCustomerQuery(customerId);

  const handleAddToCart = async (product) => {
    if (!cart) {
      console.error("Cart not available yet.");
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

  const renderContent = () => {
    if (isLoading) {
      return (
        <div className="text-center text-gray-500 py-24">
          <FontAwesomeIcon icon={faSpinner} spin size="3x" />
          <p className="mt-4 text-lg">Đang tải sản phẩm...</p>
        </div>
      );
    }

    if (error) {
      return (
        <div className="text-center text-red-500 py-24">
          <FontAwesomeIcon icon={faExclamationTriangle} size="3x" />
          <p className="mt-4 text-lg">
            Đã xảy ra lỗi khi tải sản phẩm. Vui lòng thử lại.
          </p>
        </div>
      );
    }

    if (productData && productData.content.length > 0) {
      return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
          {productData.content.map((product, index) => (
            <motion.div
              key={product.id}
              initial={{ opacity: 0, y: 50 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5, delay: index * 0.05 }}
            >
              <Link to={`/products/${product.id}`}>
                <ProductCard
                  product={product}
                  onAddToCart={() => handleAddToCart(product)}
                />
              </Link>
            </motion.div>
          ))}
        </div>
      );
    }

    return (
      <div className="text-center text-gray-500 py-24">
        <p className="text-lg">Không tìm thấy sản phẩm nào.</p>
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-indigo-50">
      <Header />

      <main className="container mx-auto px-4 py-12">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center mb-12"
        >
          <h1 className="text-5xl font-extrabold text-gray-800">
            Khám Phá Bộ Sưu Tập
          </h1>
          <p className="text-xl text-gray-600 mt-4 max-w-2xl mx-auto">
            Những sản phẩm tốt nhất, được tuyển chọn kỹ lưỡng dành riêng cho
            bạn.
          </p>
        </motion.div>

        <div className="mb-8 flex flex-col md:flex-row justify-between items-center gap-4">
          <div className="relative w-full md:max-w-md">
            <input
              type="text"
              placeholder="Tìm kiếm sản phẩm..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full pl-4 pr-10 py-3 rounded-full border-2 border-gray-200 focus:border-indigo-500 focus:outline-none transition-colors"
            />
          </div>
          <div className="flex items-center gap-4">
            <button className="flex items-center gap-2 px-4 py-2 bg-white rounded-full shadow-md hover:bg-gray-100 transition-colors">
              <FontAwesomeIcon icon={faFilter} className="text-indigo-600" />
              <span>Lọc</span>
            </button>
            <button className="flex items-center gap-2 px-4 py-2 bg-white rounded-full shadow-md hover:bg-gray-100 transition-colors">
              <FontAwesomeIcon icon={faSort} className="text-indigo-600" />
              <span>Sắp xếp</span>
            </button>
          </div>
        </div>

        {renderContent()}
      </main>

      <Footer />
    </div>
  );
};

export default ProductPage;
