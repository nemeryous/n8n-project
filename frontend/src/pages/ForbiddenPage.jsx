import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLock, faHome } from "@fortawesome/free-solid-svg-icons";
import MinimalHeader from "../components/layout/MinimalHeader";

const ForbiddenPage = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <MinimalHeader />
      <div className="flex-grow flex items-center justify-center">
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center max-w-md mx-auto px-4"
        >
          <motion.div
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ delay: 0.2, type: "spring" }}
            className="mb-8"
          >
            <FontAwesomeIcon icon={faLock} className="text-8xl text-red-500" />
          </motion.div>

          <h1 className="text-6xl font-bold text-gray-800 mb-4">403</h1>
          <h2 className="text-2xl font-semibold text-gray-700 mb-4">
            Truy cập bị từ chối
          </h2>
          <p className="text-gray-600 mb-8">
            Bạn không có quyền truy cập trang này. Vui lòng liên hệ quản trị
            viên nếu bạn cần quyền truy cập.
          </p>

          <Link to="/">
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="inline-flex items-center px-6 py-3 bg-indigo-600 text-white font-semibold rounded-lg shadow-lg hover:bg-indigo-700 transition-colors"
            >
              <FontAwesomeIcon icon={faHome} className="mr-2" />
              Về trang chủ
            </motion.button>
          </Link>
        </motion.div>
      </div>
    </div>
  );
};

export default ForbiddenPage;
