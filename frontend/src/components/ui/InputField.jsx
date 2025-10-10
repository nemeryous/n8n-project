import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faExclamationCircle } from "@fortawesome/free-solid-svg-icons";

const InputField = ({
  label,
  name, // Thêm name vào props
  type = "text",
  value,
  onChange,
  placeholder,
  required = false,
  error,
}) => {
  const commonProps = {
    name, // Truyền name vào input/textarea
    value,
    onChange,
    placeholder,
    required,
    className: `w-full px-4 py-3 border-2 rounded-lg transition-colors focus:outline-none focus:ring-2 focus:ring-indigo-500 ${
      error ? "border-red-500" : "border-gray-200"
    } bg-gray-50`,
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="mb-4"
    >
      <label className="block text-gray-700 font-semibold mb-2">
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      {type === "textarea" ? (
        <textarea {...commonProps} rows="4" />
      ) : (
        <input type={type} {...commonProps} />
      )}
      {error && (
        <motion.p
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          className="mt-2 text-sm text-red-600 flex items-center"
        >
          <FontAwesomeIcon icon={faExclamationCircle} className="mr-2" />
          {error}
        </motion.p>
      )}
    </motion.div>
  );
};

export default InputField;
