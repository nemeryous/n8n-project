import { Navigate } from "react-router-dom";
import { useSelector } from "react-redux";
import ForbiddenPage from "../pages/ForbiddenPage";

const ProtectedRoute = ({ children, requiredRole = null }) => {
  const { user, isAuthenticated } = useSelector((state) => state.auth);

  // Nếu chưa đăng nhập, redirect về trang login
  if (!isAuthenticated || !user) {
    return <Navigate to="/login" replace />;
  }

  // Nếu yêu cầu role cụ thể và user không có role đó
  if (requiredRole && user.role !== requiredRole) {
    return <ForbiddenPage />;
  }

  // Cho phép truy cập
  return children;
};

export default ProtectedRoute;
