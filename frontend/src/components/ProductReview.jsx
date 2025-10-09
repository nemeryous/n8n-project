import React, { useState } from "react";
import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar as faStarSolid } from "@fortawesome/free-solid-svg-icons";
import { faStar as faStarRegular } from "@fortawesome/free-regular-svg-icons";

const StarRating = ({ rating, setRating }) => {
  return (
    <div className="flex space-x-1">
      {[1, 2, 3, 4, 5].map((star) => (
        <motion.div
          key={star}
          whileHover={{ scale: 1.2 }}
          whileTap={{ scale: 0.9 }}
          onClick={() => setRating(star)}
          className="cursor-pointer"
        >
          <FontAwesomeIcon
            icon={star <= rating ? faStarSolid : faStarRegular}
            className="text-yellow-400 text-2xl"
          />
        </motion.div>
      ))}
    </div>
  );
};

const ProductReview = () => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    // In a real app, you would submit this data to your backend
    console.log({ rating, comment });
    alert("Cảm ơn bạn đã đánh giá sản phẩm!");
    setRating(0);
    setComment("");
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 50 }}
      animate={{ opacity: 1, y: 0 }}
      className="mt-16"
    >
      <h2 className="text-3xl font-bold text-gray-800 mb-6">
        Viết đánh giá của bạn
      </h2>
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-2xl shadow-lg"
      >
        <div className="mb-6">
          <label className="block text-gray-700 font-semibold mb-2">
            1. Bạn đánh giá sản phẩm này thế nào?
          </label>
          <StarRating rating={rating} setRating={setRating} />
        </div>

        <div className="mb-6">
          <label
            htmlFor="comment"
            className="block text-gray-700 font-semibold mb-2"
          >
            2. Viết bình luận của bạn
          </label>
          <textarea
            id="comment"
            rows="5"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Sản phẩm này thật tuyệt vời..."
            className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:border-indigo-500 focus:outline-none transition-colors bg-white resize-none"
          ></textarea>
        </div>

        <motion.button
          type="submit"
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
          disabled={rating === 0 || !comment.trim()}
          className="px-8 py-3 rounded-xl font-bold text-white shadow-lg transition-all disabled:bg-gray-400 disabled:cursor-not-allowed bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700"
        >
          Gửi đánh giá
        </motion.button>
      </form>
    </motion.div>
  );
};

export default ProductReview;
