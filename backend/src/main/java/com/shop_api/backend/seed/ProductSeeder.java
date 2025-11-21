package com.shop_api.backend.seed;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import com.github.javafaker.Faker;
import com.shop_api.backend.entity.Product;
import com.shop_api.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Product Seeder để tạo 1000 sản phẩm mẫu vào database Sử dụng Java Faker để generate dữ liệu ngẫu
 * nhiên Sử dụng Unsplash Source API để lấy hình ảnh
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final Faker faker = new Faker(new Locale("vi"));
    private final Random random = new Random();

    @Value("${app.seeding.enabled:true}")
    private boolean seedingEnabled;

    @Value("${app.seeding.product-count:1000}")
    private int productCount;

    // Danh sách categories đa dạng
    private static final String[] CATEGORIES = {"Điện thoại", "Laptop", "Tablet", "Tai nghe", "Loa",
            "Chuột", "Bàn phím", "Màn hình", "Camera", "Máy ảnh", "Đồng hồ", "Túi xách", "Giày dép",
            "Quần áo", "Phụ kiện", "Đồ gia dụng", "Nội thất", "Thể thao", "Sách", "Đồ chơi",
            "Mỹ phẩm", "Thực phẩm", "Đồ uống", "Thiết bị y tế", "Xe cộ"};

    // Unsplash Source API - không cần API key, random images
    private static final String UNSPLASH_SOURCE_BASE = "https://source.unsplash.com/800x600/?";
    // Hoặc sử dụng Picsum Photos (alternative)
    private static final String PICSUM_BASE = "https://picsum.photos/800/600?random=";

    @Override
    public void run(final String... args) {
        // Kiểm tra xem seeding có được bật không
        if (!seedingEnabled) {
            log.info("Seeding đã bị tắt trong cấu hình. Bỏ qua seeding products.");
            return;
        }

        // Chỉ chạy nếu database trống (không có products)
        if (productRepository.count() > 0) {
            log.info("Database đã có dữ liệu. Bỏ qua seeding products.");
            return;
        }

        log.info("Bắt đầu seeding {} products...", productCount);
        final long startTime = System.currentTimeMillis();

        final List<Product> products = new ArrayList<>();
        final int batchSize = 100; // Lưu theo batch để tối ưu performance

        for (int i = 1; i <= productCount; i++) {
            final Product product = createRandomProduct(i);
            products.add(product);

            // Lưu theo batch để tránh memory issue
            if (products.size() >= batchSize) {
                productRepository.saveAll(products);
                log.debug("Đã lưu batch {} products", products.size());
                products.clear();
            }

            // Log progress mỗi 100 products
            if (i % 100 == 0) {
                log.info("Đã tạo {} / {} products", i, productCount);
            }
        }

        // Lưu phần còn lại
        if (!products.isEmpty()) {
            productRepository.saveAll(products);
        }

        final long endTime = System.currentTimeMillis();
        final long duration = endTime - startTime;
        log.info("Hoàn thành seeding {} products trong {} ms ({} giây)", productCount, duration,
                duration / 1000.0);
    }

    /**
     * Tạo một product ngẫu nhiên
     *
     * @param index số thứ tự (để tạo image URL unique)
     * @return Product entity
     */
    private Product createRandomProduct(final int index) {
        final Product product = new Product();

        // Tên sản phẩm - sử dụng Commerce để có tên sản phẩm hợp lý
        product.setName(generateProductName());

        // Mô tả - sử dụng Lorem để tạo mô tả dài
        product.setDescription(generateDescription());

        // Giá - từ 50,000 đến 50,000,000 VNĐ
        product.setPrice(generatePrice());

        // Số lượng tồn kho - từ 0 đến 500
        product.setStockQuantity(random.nextInt(501));

        // Category - random từ danh sách
        final String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
        product.setCategory(category);

        // Image URL - sử dụng Unsplash Source hoặc Picsum, phù hợp với category
        product.setImageUrl(generateImageUrl(index, category));

        // Currency - mặc định VND cho Việt Nam
        product.setCurrency("VND");

        return product;
    }

    /**
     * Generate tên sản phẩm ngẫu nhiên
     *
     * @return tên sản phẩm
     */
    private String generateProductName() {
        final String[] prefixes =
                {"Premium", "Pro", "Ultra", "Super", "Deluxe", "Classic", "Modern", "Smart",
                        "Advanced", "Elite", "Luxury", "Standard", "Basic", "Professional"};

        final String[] productTypes = {faker.commerce().productName(),
                faker.commerce().material() + " " + faker.commerce().productName(),
                faker.commerce().color() + " " + faker.commerce().productName()};

        // 50% có prefix, 50% không có
        if (random.nextBoolean()) {
            return prefixes[random.nextInt(prefixes.length)] + " "
                    + productTypes[random.nextInt(productTypes.length)];
        } else {
            return productTypes[random.nextInt(productTypes.length)];
        }
    }

    /**
     * Generate mô tả sản phẩm
     *
     * @return mô tả
     */
    private String generateDescription() {
        final StringBuilder description = new StringBuilder();

        // Thêm 2-4 câu mô tả
        final int sentenceCount = 2 + random.nextInt(3);
        for (int i = 0; i < sentenceCount; i++) {
            description.append(faker.lorem().sentence(10 + random.nextInt(15)));
            if (i < sentenceCount - 1) {
                description.append(" ");
            }
        }

        // Thêm một số tính năng nổi bật
        description.append(" Tính năng nổi bật: ");
        description.append(faker.lorem().word()).append(", ");
        description.append(faker.lorem().word()).append(", ");
        description.append(faker.lorem().word()).append(".");

        return description.toString();
    }

    /**
     * Generate giá ngẫu nhiên (VNĐ)
     *
     * @return giá sản phẩm
     */
    private Double generatePrice() {
        // Tạo giá từ 50,000 đến 50,000,000 VNĐ
        // Sử dụng phân phối log để có nhiều sản phẩm giá thấp hơn
        final double minPrice = 50000.0;
        final double maxPrice = 50000000.0;

        // Random với phân phối log
        final double logMin = Math.log(minPrice);
        final double logMax = Math.log(maxPrice);
        final double logRandom = logMin + (logMax - logMin) * random.nextDouble();
        final double price = Math.exp(logRandom);

        // Làm tròn đến hàng nghìn
        return Math.round(price / 1000.0) * 1000.0;
    }

    /**
     * Generate image URL từ Unsplash hoặc Picsum
     *
     * @param index số thứ tự để tạo URL unique
     * @param category category của sản phẩm
     * @return image URL
     */
    private String generateImageUrl(final int index, final String category) {
        // 50% sử dụng Unsplash Source, 50% sử dụng Picsum
        if (random.nextBoolean()) {
            // Unsplash Source - sử dụng keyword từ category
            final String keyword = getImageKeywordForCategory(category);
            return UNSPLASH_SOURCE_BASE + keyword + "&sig=" + index;
        } else {
            // Picsum Photos - random image với seed
            return PICSUM_BASE + index;
        }
    }

    /**
     * Lấy keyword cho Unsplash image dựa trên category
     *
     * @param category category của sản phẩm
     * @return keyword
     */
    private String getImageKeywordForCategory(final String category) {
        // Map category sang keyword phù hợp
        final String categoryLower = category.toLowerCase();

        if (categoryLower.contains("điện thoại") || categoryLower.contains("phone")) {
            return "smartphone";
        } else if (categoryLower.contains("laptop")) {
            return "laptop";
        } else if (categoryLower.contains("tablet")) {
            return "tablet";
        } else if (categoryLower.contains("tai nghe") || categoryLower.contains("headphone")) {
            return "headphones";
        } else if (categoryLower.contains("loa") || categoryLower.contains("speaker")) {
            return "speaker";
        } else if (categoryLower.contains("camera") || categoryLower.contains("máy ảnh")) {
            return "camera";
        } else if (categoryLower.contains("đồng hồ") || categoryLower.contains("watch")) {
            return "watch";
        } else if (categoryLower.contains("túi xách") || categoryLower.contains("bag")) {
            return "bag";
        } else if (categoryLower.contains("giày") || categoryLower.contains("shoe")) {
            return "shoes";
        } else if (categoryLower.contains("quần áo") || categoryLower.contains("clothing")) {
            return "fashion";
        } else if (categoryLower.contains("thể thao") || categoryLower.contains("sport")) {
            return "sports";
        } else if (categoryLower.contains("mỹ phẩm") || categoryLower.contains("cosmetic")) {
            return "cosmetics";
        } else if (categoryLower.contains("thực phẩm") || categoryLower.contains("food")) {
            return "food";
        } else if (categoryLower.contains("nội thất") || categoryLower.contains("furniture")) {
            return "furniture";
        } else {
            // Default keywords
            final String[] defaultKeywords = {"product", "shopping", "item", "goods", "merchandise",
                    "retail", "electronics", "lifestyle", "technology", "accessories"};
            return defaultKeywords[random.nextInt(defaultKeywords.length)];
        }
    }
}

