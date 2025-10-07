import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheckCircle } from "@fortawesome/free-solid-svg-icons";
import { motion } from "framer-motion";

const SuccessAnimation = () => {
  return (
    <motion.div
      initial={{ scale: 0 }}
      animate={{ scale: 1 }}
      transition={{ type: "spring", stiffness: 200, damping: 15 }}
      className="relative mb-8"
    >
      <motion.div
        initial={{ scale: 0 }}
        animate={{ scale: [0, 1.2, 1] }}
        transition={{ delay: 0.2, duration: 0.6 }}
        className="w-32 h-32 bg-gradient-to-br from-green-400 to-emerald-500 rounded-full flex items-center justify-center mx-auto shadow-2xl"
      >
        <FontAwesomeIcon icon={faCheckCircle} className="text-white text-6xl" />
      </motion.div>

      {/* Animated circles */}
      <motion.div
        initial={{ scale: 0, opacity: 0 }}
        animate={{ scale: [0, 1.5, 2], opacity: [0, 0.5, 0] }}
        transition={{ delay: 0.3, duration: 1.5, repeat: Infinity }}
        className="absolute inset-0 -z-10"
      >
        <div className="w-32 h-32 bg-green-400 rounded-full mx-auto" />
      </motion.div>
    </motion.div>
  );
};

export default SuccessAnimation;
