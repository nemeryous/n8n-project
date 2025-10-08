import { motion, AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheckCircle } from "@fortawesome/free-solid-svg-icons";

const SuccessModal = ({ show, orderData }) => {
  return (
    <AnimatePresence>
      {show && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4"
          onClick={(e) => e.target === e.currentTarget && null}
        >
          <motion.div
            initial={{ scale: 0.8, y: 50 }}
            animate={{ scale: 1, y: 0 }}
            exit={{ scale: 0.8, y: 50 }}
            className="bg-white rounded-3xl p-8 max-w-md w-full shadow-2xl"
          >
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ delay: 0.2, type: "spring" }}
              className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6"
            >
              <FontAwesomeIcon
                icon={faCheckCircle}
                className="text-5xl text-green-500"
              />
            </motion.div>

            <h2 className="text-3xl font-bold text-center text-gray-800 mb-4">
              Đặt hàng thành công!
            </h2>

            <p className="text-center text-gray-600 mb-6">
              Cảm ơn bạn đã tin tưởng FashionHub. Chúng tôi sẽ liên hệ với bạn
              trong thời gian sớm nhất.
            </p>

            <div className="bg-gray-50 rounded-xl p-4 mb-6">
              <div className="flex justify-between mb-2">
                <span className="text-gray-600">Mã đơn hàng:</span>
                <span className="font-bold text-indigo-600">
                  #{orderData.orderId}
                </span>
              </div>
              <div className="flex justify-between mb-2">
                <span className="text-gray-600">Tổng tiền:</span>
                <span className="font-bold text-gray-800">
                  {orderData.total.toLocaleString("vi-VN")}đ
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Trạng thái:</span>
                <span className="font-bold text-yellow-600">Đang xử lý</span>
              </div>
            </div>

            <motion.button
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              onClick={() => window.location.reload()}
              className="w-full bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-3 rounded-xl font-bold shadow-lg hover:shadow-xl transition-all"
            >
              Tiếp tục mua sắm
            </motion.button>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default SuccessModal;
