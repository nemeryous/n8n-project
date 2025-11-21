import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faTag,
  faCheckCircle,
  faTimesCircle,
  faSpinner,
} from "@fortawesome/free-solid-svg-icons";
import { useValidateCouponQuery } from "../../app/couponApi";
import { useSelector } from "react-redux";

const CouponInput = ({
  onCouponApplied,
  onCouponRemoved,
  appliedCoupon,
  subtotal = 0,
}) => {
  const { user } = useSelector((state) => state.auth);
  const [couponCode, setCouponCode] = useState("");
  const [validationError, setValidationError] = useState(null);
  const [isValidating, setIsValidating] = useState(false);
  const [shouldValidate, setShouldValidate] = useState(false);
  const [validationParams, setValidationParams] = useState(null);

  const {
    data: validationResult,
    isLoading,
    error,
  } = useValidateCouponQuery(
    validationParams || {
      code: "",
      amount: 0,
      customerId: null,
    },
    {
      skip: !shouldValidate || !validationParams,
    },
  );

  const handleApply = async () => {
    if (!couponCode.trim()) {
      setValidationError("Vui lòng nhập mã giảm giá");
      return;
    }

    console.log("CouponInput - handleApply called:", {
      couponCode: couponCode.trim(),
      subtotal,
      customerId: user?.id,
    });

    if (subtotal <= 0) {
      console.error("Subtotal is 0 or negative:", {
        subtotal,
        couponCode: couponCode.trim(),
        type: typeof subtotal,
      });
      setValidationError("Giỏ hàng trống, không thể áp dụng coupon");
      return;
    }

    setValidationError(null);
    setIsValidating(true);

    // Set params and trigger query
    const params = {
      code: couponCode.trim(),
      amount: subtotal,
      customerId: user?.id,
    };

    console.log("Validating coupon:", params);
    setValidationParams(params);
    setShouldValidate(true);
  };

  // Handle validation result
  useEffect(() => {
    if (validationResult && isValidating && shouldValidate) {
      setIsValidating(false);
      setShouldValidate(false);
      console.log("Validation result received:", validationResult);

      // Support both camelCase and snake_case for backward compatibility
      const isValid = validationResult.isValid ?? validationResult.is_valid;
      const discountAmount =
        validationResult.discountAmount ?? validationResult.discount_amount;
      const finalAmount =
        validationResult.finalAmount ?? validationResult.final_amount;

      if (isValid) {
        onCouponApplied({
          code: validationParams?.code || couponCode.trim(),
          discountAmount: discountAmount || 0,
          finalAmount: finalAmount || 0,
          validation: validationResult,
        });
        setCouponCode("");
        setValidationError(null);
        setValidationParams(null);
      } else {
        const errorMessage =
          validationResult.message || "Mã giảm giá không hợp lệ";
        setValidationError(errorMessage);
        console.warn("Coupon validation failed:", errorMessage);
        setValidationParams(null);
      }
    }
  }, [
    validationResult,
    isValidating,
    shouldValidate,
    validationParams,
    couponCode,
    onCouponApplied,
  ]);

  // Handle error from RTK Query
  useEffect(() => {
    if (error && isValidating && shouldValidate) {
      setIsValidating(false);
      setShouldValidate(false);
      console.error("RTK Query error:", error);
      const errorMessage =
        error.data?.message || error.message || "Mã giảm giá không hợp lệ";
      setValidationError(errorMessage);
      setValidationParams(null);
    }
  }, [error, isValidating, shouldValidate]);

  const handleRemove = () => {
    onCouponRemoved();
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("vi-VN").format(amount || 0);
  };

  if (appliedCoupon) {
    return (
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-green-50 border-2 border-green-200 rounded-lg p-4"
      >
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <FontAwesomeIcon
              icon={faCheckCircle}
              className="text-green-600 text-xl"
            />
            <div>
              <p className="font-semibold text-green-800">
                Mã giảm giá: {appliedCoupon.code}
              </p>
              <p className="text-sm text-green-600">
                Giảm: {formatCurrency(appliedCoupon.discountAmount)}đ
              </p>
            </div>
          </div>
          <button
            onClick={handleRemove}
            className="text-red-600 hover:text-red-700 transition-colors"
          >
            <FontAwesomeIcon icon={faTimesCircle} />
          </button>
        </div>
      </motion.div>
    );
  }

  return (
    <div className="space-y-2">
      <label className="block text-sm font-semibold text-gray-700">
        Mã giảm giá
      </label>
      <div className="flex space-x-2">
        <div className="flex-1 relative">
          <FontAwesomeIcon
            icon={faTag}
            className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
          />
          <input
            type="text"
            value={couponCode}
            onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
            placeholder="Nhập mã giảm giá"
            className="w-full pl-10 pr-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
            onKeyPress={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
                handleApply();
              }
            }}
          />
        </div>
        <motion.button
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          onClick={handleApply}
          disabled={!couponCode.trim() || isLoading || isValidating}
          className="px-6 py-3 bg-indigo-600 text-white rounded-lg font-semibold hover:bg-indigo-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
        >
          {isLoading || isValidating ? (
            <FontAwesomeIcon icon={faSpinner} spin />
          ) : (
            "Áp dụng"
          )}
        </motion.button>
      </div>

      {validationError && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="bg-red-50 border border-red-200 text-red-700 px-4 py-2 rounded-lg text-sm"
        >
          {validationError}
        </motion.div>
      )}
    </div>
  );
};

export default CouponInput;
