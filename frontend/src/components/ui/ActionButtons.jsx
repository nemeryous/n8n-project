import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPrint, faHome } from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

const ActionButtons = () => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.8 }}
      className="flex flex-col sm:flex-row gap-4 mt-8"
    >
      <motion.button
        whileHover={{ scale: 1.02 }}
        whileTap={{ scale: 0.98 }}
        onClick={() => window.print()}
        className="flex-1 bg-white border-2 border-indigo-600 text-indigo-600 py-4 rounded-xl font-bold shadow-lg hover:bg-indigo-50 transition-all flex items-center justify-center"
      >
        <FontAwesomeIcon icon={faPrint} className="mr-2" />
        In đơn hàng
      </motion.button>

      <Link to="/" className="flex-1">
        <motion.button
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
          className="w-full bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-4 rounded-xl font-bold shadow-xl hover:shadow-2xl transition-all flex items-center justify-center"
        >
          <FontAwesomeIcon icon={faHome} className="mr-2" />
          Tiếp tục mua sắm
        </motion.button>
      </Link>
    </motion.div>
  );
};

export default ActionButtons;
