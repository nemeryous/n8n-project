import React, { useState } from "react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";
import ProductCard from "../components/ProductCard";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFilter, faSort } from "@fortawesome/free-solid-svg-icons";

// Mock data - In production, this would be fetched from an API
const mockProducts = [
  {
    id: 1,
    name: "Áo Thun Premium Cotton",
    price: 250000,
    image:
      "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop",
    rating: 4.5,
    sold: 1200,
  },
  {
    id: 2,
    name: "Quần Jean Slim Fit",
    price: 450000,
    image:
      "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400&h=400&fit=crop",
    rating: 4.8,
    sold: 850,
  },
  {
    id: 3,
    name: "Giày Sneaker Sport",
    price: 680000,
    image:
      "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400&h=400&fit=crop",
    rating: 4.7,
    sold: 2100,
  },
  {
    id: 4,
    name: "Túi Xách Mini Elegant",
    price: 320000,
    image:
      "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=400&h=400&fit=crop",
    rating: 4.6,
    sold: 950,
  },
  {
    id: 5,
    name: "Đồng Hồ Classic Watch",
    price: 890000,
    image:
      "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop",
    rating: 4.9,
    sold: 650,
  },
  {
    id: 6,
    name: "Kính Mát UV Protection",
    price: 280000,
    image:
      "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400&h=400&fit=crop",
    rating: 4.4,
    sold: 1500,
  },
  {
    id: 7,
    name: "Áo Khoác Bomber",
    price: 750000,
    image:
      "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&h=400&fit=crop",
    rating: 4.8,
    sold: 780,
  },
  {
    id: 8,
    name: "Balo Laptop Chống Nước",
    price: 550000,
    image:
      "https://images.unsplash.com/photo-1553062407-98eeb68c6a62?w=400&h=400&fit=crop",
    rating: 4.9,
    sold: 1100,
  },
];

const ProductPage = () => {
  const [cartCount, setCartCount] = useState(0);

  const handleAddToCart = (e, product) => {
    e.preventDefault(); // Prevent navigation when clicking the add to cart button
    setCartCount(cartCount + 1);
    console.log("Added to cart:", product.name);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-indigo-50">
      <Header cartCount={cartCount} />

      <main className="container mx-auto px-4 py-12">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center mb-12"
        >
          <h1 className="text-5xl font-extrabold text-gray-800 mb-2">
            Tất cả sản phẩm
          </h1>
          <p className="text-lg text-gray-600">
            Khám phá bộ sưu tập thời trang mới nhất từ FashionHub
          </p>
        </motion.div>

        {/* Filter and Sort Bar */}
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="flex flex-col md:flex-row justify-between items-center mb-8 p-4 bg-white/50 rounded-xl shadow"
        >
          <div className="flex items-center space-x-4">
            <button className="flex items-center space-x-2 text-gray-700 font-medium hover:text-indigo-600">
              <FontAwesomeIcon icon={faFilter} />
              <span>Bộ lọc</span>
            </button>
          </div>
          <div className="flex items-center space-x-2 text-gray-700 font-medium">
            <FontAwesomeIcon icon={faSort} />
            <label htmlFor="sort">Sắp xếp theo:</label>
            <select
              id="sort"
              className="form-select rounded-lg border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
            >
              <option>Mới nhất</option>
              <option>Giá: Thấp đến cao</option>
              <option>Giá: Cao đến thấp</option>
              <option>Bán chạy nhất</option>
            </select>
          </div>
        </motion.div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          {mockProducts.map((product, index) => (
            <motion.div
              key={product.id}
              initial={{ opacity: 0, y: 50 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.05 }}
            >
              <Link to={`/products/${product.id}`}>
                <ProductCard
                  product={product}
                  onAddToCart={(e) => handleAddToCart(e, product)}
                />
              </Link>
            </motion.div>
          ))}
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default ProductPage;
