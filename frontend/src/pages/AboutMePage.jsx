import React from "react";
import { motion } from "framer-motion";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUser,
  faCode,
  faPalette,
  faRocket,
} from "@fortawesome/free-solid-svg-icons";

const AboutMePage = () => {
  const skills = [
    { name: "React", level: "90%" },
    { name: "Node.js", level: "85%" },
    { name: "UI/UX Design", level: "80%" },
    { name: "Database Management", level: "75%" },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-indigo-50">
      <Header cartCount={0} />

      <main className="container mx-auto px-4 py-12">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center mb-16"
        >
          <h1 className="text-5xl font-extrabold text-gray-800 mb-2">
            Về Chúng Tôi
          </h1>
          <p className="text-lg text-gray-600">
            Niềm đam mê tạo ra những sản phẩm kỹ thuật số tuyệt vời
          </p>
        </motion.div>

        <div className="grid md:grid-cols-5 gap-12 items-center">
          {/* Profile Image */}
          <motion.div
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.2 }}
            className="md:col-span-2"
          >
            <img
              src="https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=500&h=500&fit=crop&q=80"
              alt="Your Name"
              className="rounded-full shadow-2xl mx-auto w-64 h-64 md:w-full md:h-auto object-cover"
            />
          </motion.div>

          {/* Introduction */}
          <motion.div
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.4 }}
            className="md:col-span-3"
          >
            <h2 className="text-3xl font-bold text-gray-800 mb-4 flex items-center">
              <FontAwesomeIcon icon={faUser} className="mr-3 text-indigo-600" />
              Xin chào!
            </h2>
            <p className="text-gray-700 leading-relaxed mb-4">
              Chúng tôi là một đội ngũ các nhà phát triển và thiết kế đầy nhiệt
              huyết, chuyên tạo ra các ứng dụng web hiện đại, thân thiện với
              người dùng. Với kinh nghiệm trong cả frontend và backend, chúng
              tôi biến những ý tưởng phức tạp thành các giải pháp đơn giản và
              thanh lịch.
            </p>
            <p className="text-gray-700 leading-relaxed">
              Dự án FashionHub này là một minh chứng cho khả năng của chúng tôi
              trong việc xây dựng một trang thương mại điện tử hoàn chỉnh, từ
              giao diện người dùng đến logic nghiệp vụ.
            </p>
          </motion.div>
        </div>

        {/* Skills Section */}
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.6 }}
          className="mt-20"
        >
          <h2 className="text-3xl font-bold text-center text-gray-800 mb-10">
            Kỹ năng & Chuyên môn
          </h2>
          <div className="space-y-6 max-w-2xl mx-auto">
            {skills.map((skill, index) => (
              <div key={index}>
                <div className="flex justify-between mb-1">
                  <span className="font-medium text-gray-700">
                    {skill.name}
                  </span>
                  <span className="text-sm font-medium text-indigo-600">
                    {skill.level}
                  </span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2.5">
                  <motion.div
                    initial={{ width: 0 }}
                    animate={{ width: skill.level }}
                    transition={{ delay: 0.8 + index * 0.1, duration: 0.8 }}
                    className="bg-gradient-to-r from-indigo-500 to-purple-500 h-2.5 rounded-full"
                  ></motion.div>
                </div>
              </div>
            ))}
          </div>
        </motion.div>

        {/* Philosophy Section */}
        <div className="grid md:grid-cols-3 gap-8 mt-20 text-center">
          {[
            {
              icon: faCode,
              title: "Mã nguồn sạch",
              desc: "Viết mã dễ đọc, dễ bảo trì và có khả năng mở rộng.",
            },
            {
              icon: faPalette,
              title: "Thiết kế tinh tế",
              desc: "Tập trung vào trải nghiệm người dùng và giao diện đẹp mắt.",
            },
            {
              icon: faRocket,
              title: "Hiệu suất cao",
              desc: "Tối ưu hóa ứng dụng để mang lại tốc độ nhanh nhất.",
            },
          ].map((item, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 1 + index * 0.2 }}
              className="bg-white p-8 rounded-2xl shadow-lg"
            >
              <FontAwesomeIcon
                icon={item.icon}
                className="text-4xl text-indigo-600 mb-4"
              />
              <h3 className="text-xl font-bold text-gray-800 mb-2">
                {item.title}
              </h3>
              <p className="text-gray-600">{item.desc}</p>
            </motion.div>
          ))}
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default AboutMePage;
