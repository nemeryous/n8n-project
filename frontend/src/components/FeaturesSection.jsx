import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShippingFast,
  faShoppingCart,
  faTag,
} from "@fortawesome/free-solid-svg-icons";

const FeaturesSection = () => {
  const features = [
    {
      icon: faShippingFast,
      title: "Miễn phí vận chuyển",
      desc: "Đơn hàng từ 500k",
    },
    {
      icon: faShoppingCart,
      title: "Đổi trả dễ dàng",
      desc: "Trong vòng 30 ngày",
    },
    { icon: faTag, title: "Giá tốt nhất", desc: "Cam kết hoàn tiền" },
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 my-16">
      {features.map((feature, index) => (
        <motion.div
          key={index}
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: index * 0.2 }}
          whileHover={{ y: -5 }}
          className="bg-gradient-to-br from-indigo-50 to-purple-50 p-8 rounded-2xl text-center shadow-md hover:shadow-xl transition-all"
        >
          <motion.div
            whileHover={{ rotate: 360, scale: 1.2 }}
            transition={{ duration: 0.6 }}
            className="w-16 h-16 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-full flex items-center justify-center mx-auto mb-4"
          >
            <FontAwesomeIcon
              icon={feature.icon}
              className="text-white text-2xl"
            />
          </motion.div>
          <h3 className="font-bold text-xl text-gray-800 mb-2">
            {feature.title}
          </h3>
          <p className="text-gray-600">{feature.desc}</p>
        </motion.div>
      ))}
    </div>
  );
};

export default FeaturesSection;
