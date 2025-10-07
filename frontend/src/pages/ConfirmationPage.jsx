import { useLocation, Navigate } from "react-router-dom";
import { motion } from "framer-motion";
import {
  faIdCard,
  faTruck,
  faMapMarkerAlt,
  faMoneyBillWave,
} from "@fortawesome/free-solid-svg-icons";

import MinimalHeader from "../components/layout/MinimalHeader";
import SuccessAnimation from "../components/ui/SuccessAnimation";
import OrderDetailCard from "../components/ui/OrderDetailCard";
import OrderItem from "../components/OrderItem";
import StatusTimeline from "../components/ui/StatusTimeline";
import InfoBanner from "../components/ui/InfoBanner";
import ActionButtons from "../components/ui/ActionButtons";

const ConfirmationPage = () => {
  const location = useLocation();
  const orderData = location.state?.orderData;

  // If no order data is passed, redirect to the homepage
  if (!orderData) {
    return <Navigate to="/" />;
  }

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: { y: 0, opacity: 1 },
  };

  return (
    <div className="bg-gray-50 min-h-screen font-sans">
      <MinimalHeader />

      <main className="container mx-auto px-4 py-12">
        <motion.div
          variants={containerVariants}
          initial="hidden"
          animate="visible"
          className="max-w-4xl mx-auto bg-white rounded-2xl shadow-xl p-8"
        >
          <motion.div variants={itemVariants}>
            <SuccessAnimation />
          </motion.div>

          <motion.div variants={itemVariants} className="text-center mb-8">
            <h1 className="text-4xl font-extrabold text-gray-800 mb-2">
              Đặt hàng thành công!
            </h1>
            <p className="text-gray-600 text-lg">
              Cảm ơn bạn đã tin tưởng và mua sắm tại FashionHub.
            </p>
          </motion.div>

          <motion.div variants={itemVariants}>
            <InfoBanner email={orderData.customer_email} />
          </motion.div>

          <motion.div variants={itemVariants}>
            <StatusTimeline />
          </motion.div>

          <motion.div
            variants={itemVariants}
            className="grid md:grid-cols-2 gap-6 mb-8"
          >
            <OrderDetailCard
              icon={faIdCard}
              label="Mã đơn hàng"
              value={orderData.order_id}
              highlight
            />
            <OrderDetailCard
              icon={faMoneyBillWave}
              label="Tổng thanh toán"
              value={`${orderData.total_amount.toLocaleString("vi-VN")}đ`}
              highlight
            />
            <OrderDetailCard
              icon={faTruck}
              label="Khách hàng"
              value={orderData.customer_name}
            />
            <OrderDetailCard
              icon={faMapMarkerAlt}
              label="Địa chỉ giao hàng"
              value={orderData.shipping_address}
            />
          </motion.div>

          <motion.div variants={itemVariants} className="mb-8">
            <h3 className="text-xl font-bold text-gray-800 mb-4">
              Chi tiết sản phẩm
            </h3>
            <div className="bg-gray-50 rounded-xl p-4">
              {orderData.items.map((item, index) => (
                <OrderItem key={index} item={item} index={index} />
              ))}
            </div>
          </motion.div>

          <motion.div variants={itemVariants}>
            <ActionButtons />
          </motion.div>
        </motion.div>
      </main>
    </div>
  );
};

export default ConfirmationPage;
