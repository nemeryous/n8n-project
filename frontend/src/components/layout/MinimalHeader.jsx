import { motion } from "framer-motion";

const MinimalHeader = () => {
  return (
    <motion.header
      initial={{ y: -50, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      className="bg-white shadow-md"
    >
      <div className="container mx-auto px-4 py-6">
        <motion.div
          whileHover={{ scale: 1.05 }}
          className="text-3xl font-bold tracking-tight cursor-pointer inline-block"
        >
          <span className="bg-gradient-to-r from-indigo-600 to-purple-600 text-transparent bg-clip-text">
            Fashion
          </span>
          <span className="text-yellow-500">Hub</span>
        </motion.div>
      </div>
    </motion.header>
  );
};

export default MinimalHeader;
