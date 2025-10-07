import React, { useState } from "react";
import { motion } from "framer-motion";
import Header from "../components/layout/Header";
import HeroBanner from "../components/HeroBanner";
import FeaturesSection from "../components/FeaturesSection";
import ProductCard from "../components/ProductCard";
import Footer from "../components/layout/Footer";

// Mock data - Trong production sẽ fetch từ RTK Query
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
];

export default function HomePage() {
  const [cartCount, setCartCount] = useState(0);
  const [cartItems, setCartItems] = useState([]);

  const handleAddToCart = (product) => {
    // Trong production: Gọi RTK Query mutation để thêm vào cart_items
    // Logic:
    // 1. Kiểm tra session_id hoặc customer_id
    // 2. Tìm/tạo cart với status='active'
    // 3. Thêm/cập nhật cart_items
    // 4. Cập nhật UI

    const existingItem = cartItems.find((item) => item.id === product.id);

    if (existingItem) {
      setCartItems(
        cartItems.map((item) =>
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      );
    } else {
      setCartItems([...cartItems, { ...product, quantity: 1 }]);
    }

    setCartCount(cartCount + 1);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-indigo-50">
      <Header cartCount={cartCount} />

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
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="hidden md:block text-indigo-600 font-semibold hover:text-indigo-700 transition-colors"
            >
              Xem tất cả →
            </motion.button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {mockProducts.map((product, index) => (
              <motion.div
                key={product.id}
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.1 }}
              >
                <ProductCard product={product} onAddToCart={handleAddToCart} />
              </motion.div>
            ))}
          </div>
        </motion.div>
      </main>

      <Footer />
    </div>
  );
}
