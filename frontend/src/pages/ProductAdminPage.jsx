import React, { useState } from "react";
import { AnimatePresence } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faPlus,
  faEdit,
  faTrash,
  faBoxOpen,
  faTimes,
  faSpinner,
  faExclamationTriangle,
} from "@fortawesome/free-solid-svg-icons";
import {
  useGetProductsQuery,
  useCreateProductMutation,
  useUpdateProductMutation,
  useDeleteProductMutation,
} from "../app/productApi";
import InputField from "../components/ui/InputField";
import { motion } from "framer-motion";

// Modal Form Component
const ProductFormModal = ({ product, onClose, onSave, isLoading }) => {
  const [formData, setFormData] = useState({
    productName: product?.name || "",
    productDescription: product?.description || "",
    price: product?.price || 0,
    stock: product?.stock_quantity || 0,
    category: product?.category || "",
    imageUrl: product?.image_url || "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center z-50 p-4"
      onClick={onClose}
    >
      <motion.div
        initial={{ y: -50, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        exit={{ y: 50, opacity: 0 }}
        className="bg-white rounded-2xl shadow-2xl w-full max-w-2xl p-8"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">
            {product ? "Chỉnh sửa sản phẩm" : "Thêm sản phẩm mới"}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <FontAwesomeIcon icon={faTimes} size="lg" />
          </button>
        </div>
        <form onSubmit={handleSubmit} className="space-y-4">
          <InputField
            label="Tên sản phẩm"
            name="productName"
            value={formData.productName}
            onChange={handleChange}
            required
          />
          <InputField
            label="Mô tả"
            name="productDescription"
            value={formData.productDescription}
            onChange={handleChange}
            type="textarea"
          />
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <InputField
              label="Giá"
              name="price"
              type="number"
              value={formData.price}
              onChange={handleChange}
              required
            />
            <InputField
              label="Số lượng tồn kho"
              name="stock"
              type="number"
              value={formData.stock}
              onChange={handleChange}
              required
            />
          </div>
          <InputField
            label="Danh mục"
            name="category"
            value={formData.category}
            onChange={handleChange}
          />
          <InputField
            label="URL Hình ảnh"
            name="imageUrl"
            value={formData.imageUrl}
            onChange={handleChange}
          />
          <div className="flex justify-end pt-4">
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              type="button"
              onClick={onClose}
              className="mr-4 px-6 py-2 rounded-lg text-gray-700 bg-gray-100 hover:bg-gray-200 font-semibold"
            >
              Hủy
            </motion.button>
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              type="submit"
              disabled={isLoading}
              className="px-6 py-2 rounded-lg bg-indigo-600 text-white font-semibold hover:bg-indigo-700 disabled:bg-indigo-300 flex items-center"
            >
              {isLoading && (
                <FontAwesomeIcon icon={faSpinner} spin className="mr-2" />
              )}
              Lưu
            </motion.button>
          </div>
        </form>
      </motion.div>
    </motion.div>
  );
};

const ProductAdminPage = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const {
    data: productsData,
    error,
    isLoading: isLoadingProducts,
  } = useGetProductsQuery();
  const [createProduct, { isLoading: isCreating }] = useCreateProductMutation();
  const [updateProduct, { isLoading: isUpdating }] = useUpdateProductMutation();
  const [deleteProduct, { isLoading: isDeleting }] = useDeleteProductMutation();

  const handleOpenModal = (product = null) => {
    setSelectedProduct(product);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedProduct(null);
  };

  const handleSave = async (formData) => {
    try {
      if (selectedProduct) {
        await updateProduct({ id: selectedProduct.id, ...formData }).unwrap();
      } else {
        await createProduct(formData).unwrap();
      }
      handleCloseModal();
    } catch (err) {
      console.error("Failed to save product:", err);
      // Hiển thị thông báo lỗi cho người dùng
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) {
      try {
        await deleteProduct(id).unwrap();
      } catch (err) {
        console.error("Failed to delete product:", err);
      }
    }
  };

  const renderProductRows = () => {
    if (isLoadingProducts) {
      return (
        <tr>
          <td colSpan="6" className="text-center py-16">
            <FontAwesomeIcon icon={faSpinner} spin size="2x" />
            <p className="mt-2">Đang tải danh sách sản phẩm...</p>
          </td>
        </tr>
      );
    }

    if (error) {
      return (
        <tr>
          <td colSpan="6" className="text-center py-16 text-red-500">
            <FontAwesomeIcon icon={faExclamationTriangle} size="2x" />
            <p className="mt-2">Lỗi khi tải dữ liệu.</p>
          </td>
        </tr>
      );
    }

    if (!productsData || productsData.content.length === 0) {
      return (
        <tr>
          <td colSpan="6" className="text-center py-16 text-gray-500">
            <FontAwesomeIcon icon={faBoxOpen} size="2x" />
            <p className="mt-2">Chưa có sản phẩm nào.</p>
          </td>
        </tr>
      );
    }

    return productsData.content.map((product) => (
      <tr key={product.id} className="hover:bg-gray-50 transition-colors">
        <td className="p-4 border-b border-gray-200">
          <img
            src={product.image_url || "https://via.placeholder.com/80"}
            alt={product.name}
            className="w-16 h-16 object-cover rounded-lg"
          />
        </td>
        <td className="p-4 border-b border-gray-200 font-medium text-gray-800">
          {product.name}
        </td>
        <td className="p-4 border-b border-gray-200 text-gray-600">
          {product.price.toLocaleString("vi-VN")}đ
        </td>
        <td className="p-4 border-b border-gray-200 text-gray-600">
          {product.stock_quantity}
        </td>
        <td className="p-4 border-b border-gray-200 text-gray-600">
          {product.category}
        </td>
        <td className="p-4 border-b border-gray-200">
          <div className="flex space-x-2">
            <motion.button
              whileHover={{ scale: 1.1 }}
              onClick={() => handleOpenModal(product)}
              className="text-indigo-600 hover:text-indigo-800"
              aria-label="Edit"
            >
              <FontAwesomeIcon icon={faEdit} />
            </motion.button>
            <motion.button
              whileHover={{ scale: 1.1 }}
              onClick={() => handleDelete(product.id)}
              disabled={isDeleting}
              className="text-red-600 hover:text-red-800 disabled:text-gray-300"
              aria-label="Delete"
            >
              <FontAwesomeIcon icon={faTrash} />
            </motion.button>
          </div>
        </td>
      </tr>
    ));
  };

  return (
    <div className="p-8 bg-gray-50 min-h-full">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Quản lý sản phẩm</h1>
        <motion.button
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          onClick={() => handleOpenModal()}
          className="bg-indigo-600 text-white px-5 py-2 rounded-lg font-semibold shadow-md hover:bg-indigo-700 flex items-center"
        >
          <FontAwesomeIcon icon={faPlus} className="mr-2" />
          Thêm sản phẩm
        </motion.button>
      </div>

      <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead className="bg-gray-100">
              <tr>
                <th className="p-4 font-semibold text-gray-600">Hình ảnh</th>
                <th className="p-4 font-semibold text-gray-600">
                  Tên sản phẩm
                </th>
                <th className="p-4 font-semibold text-gray-600">Giá</th>
                <th className="p-4 font-semibold text-gray-600">Tồn kho</th>
                <th className="p-4 font-semibold text-gray-600">Danh mục</th>
                <th className="p-4 font-semibold text-gray-600">Hành động</th>
              </tr>
            </thead>
            <tbody>{renderProductRows()}</tbody>
          </table>
        </div>
      </div>

      <AnimatePresence>
        {isModalOpen && (
          <ProductFormModal
            product={selectedProduct}
            onClose={handleCloseModal}
            onSave={handleSave}
            isLoading={isCreating || isUpdating}
          />
        )}
      </AnimatePresence>
    </div>
  );
};

export default ProductAdminPage;
