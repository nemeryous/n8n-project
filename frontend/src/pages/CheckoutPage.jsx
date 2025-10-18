import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShoppingCart,
  faArrowLeft,
  faSpinner,
  faExclamationTriangle,
} from "@fortawesome/free-solid-svg-icons";
import Header from "../components/layout/Header";
import CartItem from "../components/CartItem";
import OrderSummary from "../components/OrderSummary";
import CheckoutForm from "../components/CheckoutForm";
import { Link, useNavigate } from "react-router-dom";
import {
  useGetCartItemsByCustomerIdQuery,
  useUpdateCartItemMutation,
  useDeleteCartItemMutation,
} from "../app/cartItemApi";
import { useGetOrCreateCartByCustomerQuery } from "../app/cartApi";
import { useCreateOrderMutation } from "../app/orderApi";

const SHIPPING_FEE = 30000;

export default function CheckoutPage() {
  const customerId = 1;
  const { data: cart, isLoading: isCartLoading } =
    useGetOrCreateCartByCustomerQuery(customerId);
  const {
    data: cartItems = [],
    isLoading: areItemsLoading,
    error: itemsError,
  } = useGetCartItemsByCustomerIdQuery(customerId, {
    skip: !cart,
  });

  const [updateCartItem] = useUpdateCartItemMutation();
  const [deleteCartItem] = useDeleteCartItemMutation();
  const [createOrder, { isLoading: isProcessing }] = useCreateOrderMutation();

  const navigate = useNavigate();

  const handleUpdateQuantity = async (itemId, newQuantity) => {
    if (newQuantity < 1) return;
    try {
      await updateCartItem({ id: itemId, quantity: newQuantity });
    } catch (error) {
      console.error("Failed to update quantity:", error);
    }
  };

  const handleRemoveItem = async (itemId) => {
    try {
      await deleteCartItem(itemId);
    } catch (error) {
      console.error("Failed to remove item:", error);
    }
  };

  const handleCheckout = async (formData) => {
    if (!cart || !cartItems.length) return;

    const orderPayload = {
      customer_id: customerId,
      cart_id: cart.id,
      shipping_address: formData.address,
      phone_number: formData.phone,
      notes: `Customer name: ${formData.name}`,
    };

    try {
      const result = await createOrder(orderPayload).unwrap();

      const subtotal = cartItems.reduce(
        (sum, item) => sum + item.product.price * item.quantity,
        0
      );
      const total = subtotal + SHIPPING_FEE;

      const finalOrderData = {
        ...result,
        total_amount: total,
        customer_name: formData.name,
        shipping_address: formData.address,
        customer_email: formData.email,
        items: cartItems.map((ci) => ({
          ...ci.product,
          quantity: ci.quantity,
        })),
        shippingFee: SHIPPING_FEE,
        subtotal: subtotal,
      };

      navigate("/confirmation", { state: { orderData: finalOrderData } });
    } catch (err) {
      console.error("Failed to create order:", err);
      // Optionally, show an error message to the user
    }
  };

  const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);

  if (isCartLoading || areItemsLoading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <FontAwesomeIcon icon={faSpinner} spin size="3x" />
      </div>
    );
  }

  if (itemsError) {
    return (
      <div className="text-center py-10">
        <FontAwesomeIcon
          icon={faExclamationTriangle}
          className="text-red-500 text-4xl mb-4"
        />
        <p className="text-red-500">Error loading cart items.</p>
      </div>
    );
  }

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
