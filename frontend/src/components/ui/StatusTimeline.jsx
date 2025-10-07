import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCheckCircle,
  faEnvelope,
  faBox,
} from "@fortawesome/free-solid-svg-icons";
import { motion } from "framer-motion";

const StatusTimeline = () => {
  const statuses = [
    { label: "Đặt hàng", icon: faCheckCircle, active: true, completed: true },
    { label: "Xác nhận", icon: faEnvelope, active: true, completed: false },
    { label: "Đang giao", icon: faBox, active: false, completed: false },
    {
      label: "Hoàn thành",
      icon: faCheckCircle,
      active: false,
      completed: false,
    },
  ];

  return (
    <div className="mb-8">
      <h3 className="text-xl font-bold text-gray-800 mb-6">
        Trạng thái đơn hàng
      </h3>
      <div className="relative">
        {/* Progress line */}
        <div className="absolute top-6 left-0 right-0 h-1 bg-gray-200" />
        <motion.div
          initial={{ width: 0 }}
          animate={{ width: "25%" }}
          transition={{ duration: 1, delay: 0.5 }}
          className="absolute top-6 left-0 h-1 bg-gradient-to-r from-green-500 to-indigo-600"
        />

        <div className="grid grid-cols-4 gap-4 relative">
          {statuses.map((status, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.2 }}
              className="flex flex-col items-center"
            >
              <motion.div
                animate={status.completed ? { scale: [1, 1.2, 1] } : {}}
                transition={{ duration: 0.5, delay: index * 0.2 }}
                className={`w-12 h-12 rounded-full flex items-center justify-center mb-2 ${
                  status.completed
                    ? "bg-gradient-to-br from-green-400 to-emerald-500 text-white"
                    : status.active
                      ? "bg-indigo-600 text-white"
                      : "bg-gray-200 text-gray-400"
                }`}
              >
                <FontAwesomeIcon icon={status.icon} />
              </motion.div>
              <p
                className={`text-sm font-medium text-center ${
                  status.active ? "text-gray-800" : "text-gray-400"
                }`}
              >
                {status.label}
              </p>
            </motion.div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default StatusTimeline;
