import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope } from "@fortawesome/free-solid-svg-icons";
import { motion } from "framer-motion";

const InfoBanner = ({ email }) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.6 }}
      className="bg-gradient-to-r from-blue-50 to-indigo-50 border-l-4 border-indigo-600 p-6 rounded-r-xl mb-8"
    >
      <div className="flex items-start space-x-4">
        <div className="w-12 h-12 bg-indigo-600 rounded-full flex items-center justify-center flex-shrink-0">
          <FontAwesomeIcon icon={faEnvelope} className="text-white text-xl" />
        </div>
        <div>
          <h4 className="font-bold text-gray-800 mb-2">
            Email xác nhận đã được gửi
          </h4>
          <p className="text-gray-600 text-sm">
            Chúng tôi đã gửi email xác nhận đơn hàng đến{" "}
            <span className="font-semibold text-indigo-600">{email}</span>. Vui
            lòng kiểm tra hộp thư (và cả thư mục spam nếu cần).
          </p>
        </div>
      </div>
    </motion.div>
  );
};

export default InfoBanner;
