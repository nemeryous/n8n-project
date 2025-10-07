import { motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFacebook,
  faInstagram,
  faTwitter,
} from "@fortawesome/free-brands-svg-icons";

const Footer = () => {
  return (
    <footer className="bg-gradient-to-r from-gray-900 via-indigo-900 to-purple-900 text-white mt-20">
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 className="text-2xl font-bold mb-4">
              <span className="text-yellow-400">Fashion</span>Hub
            </h3>
            <p className="text-gray-300 mb-4">
              Thời trang hiện đại, phong cách trẻ trung, giá cả phải chăng.
            </p>
            <div className="flex space-x-4">
              {[faFacebook, faInstagram, faTwitter].map((icon, i) => (
                <motion.a
                  key={i}
                  href="#"
                  whileHover={{ scale: 1.2, rotate: 5 }}
                  whileTap={{ scale: 0.9 }}
                  className="w-10 h-10 bg-white/10 rounded-full flex items-center justify-center hover:bg-white/20 transition-colors"
                >
                  <FontAwesomeIcon icon={icon} />
                </motion.a>
              ))}
            </div>
          </div>

          <div>
            <h4 className="font-bold text-lg mb-4">Thông tin</h4>
            <ul className="space-y-2 text-gray-300">
              <li>
                <a href="#" className="hover:text-yellow-400 transition-colors">
                  Về chúng tôi
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-yellow-400 transition-colors">
                  Liên hệ
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-yellow-400 transition-colors">
                  Tuyển dụng
                </a>
              </li>
            </ul>
          </div>

          <div>
            <h4 className="font-bold text-lg mb-4">Chính sách</h4>
            <ul className="space-y-2 text-gray-300">
              <li>
                <a href="#" className="hover:text-yellow-400 transition-colors">
                  Chính sách đổi trả
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-yellow-400 transition-colors">
                  Chính sách bảo mật
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-yellow-400 transition-colors">
                  Điều khoản sử dụng
                </a>
              </li>
            </ul>
          </div>

          <div>
            <h4 className="font-bold text-lg mb-4">Liên hệ</h4>
            <ul className="space-y-2 text-gray-300">
              <li>📞 1900 xxxx</li>
              <li>📧 support@fashionhub.vn</li>
              <li>📍 123 Đường ABC, Quận 1, TP.HCM</li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
          <p>&copy; 2025 FashionHub. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
