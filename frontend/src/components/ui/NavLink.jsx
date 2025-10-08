import { NavLink as RouterNavLink } from "react-router-dom";
import { motion } from "framer-motion";

const NavLink = ({ to, children }) => (
  <RouterNavLink
    to={to}
    className={({ isActive }) =>
      `font-medium transition-all ${
        isActive
          ? "text-yellow-300 border-b-2 border-yellow-300"
          : "hover:text-yellow-300"
      }`
    }
  >
    <motion.div whileHover={{ y: -2 }}>{children}</motion.div>
  </RouterNavLink>
);

export default NavLink;
