import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFire, faTag } from "@fortawesome/free-solid-svg-icons";

const HeroBanner = () => {
  return (
    <motion.section
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.8 }}
      className="relative h-[500px] bg-gradient-to-br from-purple-900 via-indigo-800 to-blue-900 overflow-hidden"
    >
      <div className="absolute inset-0 bg-black opacity-20"></div>

      {/* Animated background shapes */}
      <motion.div
        animate={{
          scale: [1, 1.2, 1],
          rotate: [0, 180, 360],
        }}
        transition={{ duration: 20, repeat: Infinity }}
        className="absolute top-10 right-10 w-64 h-64 bg-pink-500 rounded-full opacity-20 blur-3xl"
      />
      <motion.div
        animate={{
          scale: [1.2, 1, 1.2],
          rotate: [360, 180, 0],
        }}
        transition={{ duration: 15, repeat: Infinity }}
        className="absolute bottom-10 left-10 w-96 h-96 bg-yellow-500 rounded-full opacity-20 blur-3xl"
      />

      <div className="relative container mx-auto px-4 h-full flex items-center">
        <div className="max-w-2xl">
          <motion.div
            initial={{ x: -50, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            transition={{ delay: 0.3 }}
            className="flex items-center space-x-2 mb-4"
          >
            <FontAwesomeIcon
              icon={faFire}
              className="text-yellow-400 text-2xl"
            />
            <span className="text-yellow-400 font-semibold text-lg">
              HOT SALE
            </span>
          </motion.div>

          <motion.h1
            initial={{ x: -50, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            transition={{ delay: 0.5 }}
            className="text-6xl font-bold text-white mb-6 leading-tight"
          >
            Bộ Sưu Tập
            <br />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-yellow-400 to-pink-400">
              Mùa Hè 2025
            </span>
          </motion.h1>

          <motion.p
            initial={{ x: -50, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            transition={{ delay: 0.7 }}
            className="text-xl text-gray-200 mb-8"
          >
            Giảm giá lên đến{" "}
            <span className="text-yellow-400 font-bold text-3xl">30%</span> cho
            tất cả sản phẩm
          </motion.p>

          <motion.button
            initial={{ x: -50, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            transition={{ delay: 0.9 }}
            whileHover={{
              scale: 1.05,
              boxShadow: "0 20px 40px rgba(0,0,0,0.3)",
            }}
            whileTap={{ scale: 0.95 }}
            className="bg-gradient-to-r from-yellow-400 to-orange-500 text-gray-900 px-10 py-4 rounded-full font-bold text-lg shadow-2xl hover:shadow-yellow-500/50 transition-all"
          >
            Xem Ngay <FontAwesomeIcon icon={faTag} className="ml-2" />
          </motion.button>
        </div>
      </div>
    </motion.section>
  );
};

export default HeroBanner;
