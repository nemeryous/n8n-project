-- =================================================================
-- Mock Data for Customers and Products
-- =================================================================

-- -------------------------------------------------
-- Table `customers`
-- -------------------------------------------------
INSERT INTO `customers` (`id`, `name`, `email`, `phone`, `address`, `created_at`, `updated_at`) VALUES
(1, 'Nguyễn Văn An', 'an.nguyen@example.com', '0901234567', '123 Đường Lê Lợi, Quận 1, TP. Hồ Chí Minh', NOW(), NOW()),
(2, 'Trần Thị Bích', 'bich.tran@example.com', '0912345678', '456 Đường Nguyễn Huệ, Quận Hoàn Kiếm, Hà Nội', NOW(), NOW()),
(3, 'Lê Hoàng Cường', 'cuong.le@example.com', '0987654321', '789 Đường Trần Phú, Quận Hải Châu, Đà Nẵng', NOW(), NOW()),
(4, 'Phạm Thu Dung', 'dung.pham@example.com', '0934567890', '101 Đường Hùng Vương, Quận Ninh Kiều, Cần Thơ', NOW(), NOW()),
(5, 'Võ Minh Hải', 'hai.vo@example.com', '0945678901', '212 Đường Bà Triệu, TP. Huế', NOW(), NOW()),
(6, 'Đặng Thị Lan', 'lan.dang@example.com', '0978123456', '333 Đường Lê Duẩn, Quận Đống Đa, Hà Nội', NOW(), NOW());


-- -------------------------------------------------
-- Table `products`
-- -------------------------------------------------
INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock_quantity`, `image_url`, `created_at`, `updated_at`) VALUES
(1, 'iPhone 15 Pro Max 256GB', 'Titan tự nhiên, Chip A17 Pro, hệ thống camera chuyên nghiệp.', 29500000.00, 50, 'https://example.com/images/iphone15.jpg', NOW(), NOW()),
(2, 'Samsung Galaxy S24 Ultra 512GB', 'Galaxy AI, bút S Pen tích hợp, camera 200MP.', 31000000.00, 45, 'https://example.com/images/s24ultra.jpg', NOW(), NOW()),
(3, 'MacBook Air M3 13-inch', 'Chip Apple M3, màn hình Liquid Retina, thời lượng pin lên đến 18 giờ.', 28000000.00, 30, 'https://example.com/images/macbookairm3.jpg', NOW(), NOW()),
(4, 'Dell XPS 15 (9530)', 'Màn hình OLED 3.5K, Intel Core i7, NVIDIA RTX 4050.', 45000000.00, 25, 'https://example.com/images/dellxps15.jpg', NOW(), NOW()),
(5, 'Sony WH-1000XM5 Headphones', 'Tai nghe chống ồn chủ động hàng đầu, chất lượng âm thanh tuyệt vời.', 8500000.00, 100, 'https://example.com/images/sonyxm5.jpg', NOW(), NOW()),
(6, 'Apple Watch Series 9', 'Chip S9 SiP, tính năng Double Tap, màn hình sáng hơn.', 10500000.00, 80, 'https://example.com/images/applewatch9.jpg', NOW(), NOW()),
(7, 'Bàn phím cơ Keychron Q1 Pro', 'Layout 75%, Full-metal body, QMK/VIA support, Wireless.', 4800000.00, 60, 'https://example.com/images/keychronq1.jpg', NOW(), NOW()),
(8, 'Màn hình LG UltraGear 27GP850-B', '27 inch, Nano IPS, 1ms, 165Hz, QHD (2560x1440).', 9900000.00, 40, 'https://example.com/images/lgultragear.jpg', NOW(), NOW()),
(9, 'Máy hút bụi Dyson V15 Detect', 'Công nghệ laser phát hiện bụi, lực hút mạnh mẽ.', 19000000.00, 35, 'https://example.com/images/dysonv15.jpg', NOW(), NOW()),
(10, 'Máy chơi game Sony PlayStation 5', 'Phiên bản Standard có ổ đĩa, trải nghiệm game thế hệ mới.', 14500000.00, 20, 'https://example.com/images/ps5.jpg', NOW(), NOW());

-- You can add more data following the same pattern.