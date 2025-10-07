import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { motion } from "framer-motion";

const OrderDetailCard = ({ icon, label, value, highlight = false }) => {
  return (
    <motion.div
      initial={{ opacity: 0, x: -20 }}
      animate={{ opacity: 1, x: 0 }}
      whileHover={{ scale: 1.02 }}
      className={`p-4 rounded-xl ${
        highlight
          ? "bg-gradient-to-r from-indigo-50 to-purple-50 border-2 border-indigo-200"
          : "bg-gray-50"
      }`}
    >
      <div className="flex items-start space-x-3">
        <div
          className={`w-10 h-10 rounded-lg flex items-center justify-center flex-shrink-0 ${
            highlight ? "bg-indigo-600" : "bg-gray-200"
          }`}
        >
          <FontAwesomeIcon
            icon={icon}
            className={highlight ? "text-white" : "text-gray-600"}
          />
        </div>
        <div className="flex-1">
          <p className="text-sm text-gray-600 mb-1">{label}</p>
          <p
            className={`font-semibold ${highlight ? "text-indigo-700 text-lg" : "text-gray-800"}`}
          >
            {value}
          </p>
        </div>
      </div>
    </motion.div>
  );
};

export default OrderDetailCard;
