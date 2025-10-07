import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faExclamationCircle } from "@fortawesome/free-solid-svg-icons";

const InputField = ({
  label,
  type = "text",
  value,
  onChange,
  placeholder,
  required = true,
  error,
}) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="mb-6"
    >
      <label className="block text-gray-700 font-semibold mb-2">
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      <input
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        className={`w-full px-4 py-3 border-2 ${
          error ? "border-red-500" : "border-gray-200"
        } rounded-xl focus:border-indigo-500 focus:outline-none transition-colors bg-white`}
      />
      {error && (
        <motion.p
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-red-500 text-sm mt-1 flex items-center"
        >
          <FontAwesomeIcon icon={faExclamationCircle} className="mr-1" />
          {error}
        </motion.p>
      )}
    </motion.div>
  );
};

export default InputField;
