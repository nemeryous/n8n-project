import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faShoppingCart, faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import Header from "../components/layout/Header";
import CartItem from "../components/CartItem";
import OrderSummary from "../components/OrderSummary";
import CheckoutForm from "../components/CheckoutForm";
import { Link, useNavigate } from "react-router-dom";

// Mock cart data - Trong production sẽ fetch từ RTK Query
const mockCartItems = [
  {
    id: 1,
    product_id: 1,
    name: "Áo Thun Premium Cotton",
    price: 250000,
    quantity: 1,
    image:
      "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=150&h=150&fit=crop",
  },
  {
    id: 2,
    product_id: 2,
    name: "Quần Jean Slim Fit",
    price: 450000,
    quantity: 1,
    image:
      "https://images.unsplash.com/photo-1542272604-787c3835535d?w=150&h=150&fit=crop",
  },
];

const SHIPPING_FEE = 30000;

export default function CheckoutPage() {
  const [cartItems, setCartItems] = useState(mockCartItems);
  const [isProcessing, setIsProcessing] = useState(false);
  const navigate = useNavigate();

  const handleUpdateQuantity = (itemId, newQuantity) => {
    if (newQuantity < 1) return;

    setCartItems(
      cartItems.map((item) =>
        item.id === itemId ? { ...item, quantity: newQuantity } : item
      )
    );
  };

  const handleRemoveItem = (itemId) => {
    setCartItems(cartItems.filter((item) => item.id !== itemId));
  };

  const handleCheckout = async (formData) => {
    setIsProcessing(true);

    // In a real app, you would send this data to your backend
    // For now, we'll just simulate a delay and navigate
    await new Promise((resolve) => setTimeout(resolve, 2000));

    const subtotal = cartItems.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );
    const total = subtotal + SHIPPING_FEE;

    const finalOrderData = {
      order_id: Math.random().toString(36).substr(2, 9).toUpperCase(),
      total_amount: total,
      customer_name: formData.name,
      shipping_address: formData.address,
      customer_email: formData.email,
      items: cartItems,
      shippingFee: SHIPPING_FEE,
      subtotal: subtotal,
    };

    setIsProcessing(false);

    // Navigate to confirmation page
    navigate("/confirmation", { state: { orderData: finalOrderData } });
  };

  const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-indigo-50">
      <Header cartCount={totalItems} />

      <div className="container mx-auto px-4 py-8">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8"
        >
          <Link to="/">
            <motion.button
              whileHover={{ x: -5 }}
              className="text-indigo-600 font-semibold flex items-center mb-4 hover:text-indigo-700"
            >
              <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
              Quay lại mua sắm
            </motion.button>
          </Link>

          <h1 className="text-4xl font-bold text-gray-800">Thanh toán</h1>
        </motion.div>

        <AnimatePresence>
          {cartItems.length === 0 && (
            <motion.div
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: "auto" }}
              exit={{ opacity: 0, height: 0 }}
              className="text-center bg-white p-12 rounded-xl shadow-lg"
            >
              <FontAwesomeIcon
                icon={faShoppingCart}
                className="text-5xl text-gray-300 mb-4"
              />
              <h2 className="text-2xl font-semibold text-gray-700 mb-2">
                Giỏ hàng của bạn đang trống
              </h2>
              <p className="text-gray-500 mb-6">
                Hãy thêm sản phẩm vào giỏ hàng để tiếp tục mua sắm nhé!
              </p>
              <Link to="/">
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  className="bg-indigo-600 text-white font-bold py-3 px-8 rounded-lg shadow-md hover:bg-indigo-700 transition-colors"
                >
                  Khám phá sản phẩm
                </motion.button>
              </Link>
            </motion.div>
          )}
        </AnimatePresence>

        {cartItems.length > 0 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="grid lg:grid-cols-3 gap-8"
          >
            {/* Cột trái: Chi tiết giỏ hàng & Form */}
            <div className="lg:col-span-2 space-y-8">
              <motion.div
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                className="bg-white rounded-xl shadow-lg p-8"
              >
                <h2 className="text-2xl font-bold text-gray-800 mb-6">
                  Giỏ hàng ({totalItems} sản phẩm)
                </h2>
                <div className="space-y-4">
                  {cartItems.map((item) => (
                    <CartItem
                      key={item.id}
                      item={item}
                      onUpdateQuantity={handleUpdateQuantity}
                      onRemove={handleRemoveItem}
                    />
                  ))}
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0, transition: { delay: 0.1 } }}
                className="bg-white rounded-xl shadow-lg p-8"
              >
                <CheckoutForm
                  onSubmit={handleCheckout}
                  isProcessing={isProcessing}
                />
              </motion.div>
            </div>

            {/* Cột phải: Tóm tắt đơn hàng */}
            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0, transition: { delay: 0.2 } }}
              className="h-fit sticky top-8"
            >
              <OrderSummary cartItems={cartItems} shippingFee={SHIPPING_FEE} />
            </motion.div>
          </motion.div>
        )}
      </div>
    </div>
  );
}
