package com.shop_api.backend.specification;

import org.springframework.data.jpa.domain.Specification;

import com.shop_api.backend.entity.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
  public static Specification<Product> searchByKeyword(String keyword) {
    return (root, query, criteriaBuilder) -> {
      if (keyword == null || keyword.trim().isEmpty()) {
        return criteriaBuilder.conjunction(); // Trả về tất cả nếu không có từ khóa
      }

      String likePattern = "%" + keyword.toLowerCase() + "%";

      Predicate namePredicate = criteriaBuilder.like(
          criteriaBuilder.lower(root.get("name")), likePattern);

      Predicate descriptionPredicate = criteriaBuilder.like(
          criteriaBuilder.lower(root.get("description")), likePattern);

      Predicate categoryPredicate = criteriaBuilder.like(
          criteriaBuilder.lower(root.get("category")), likePattern);

      return criteriaBuilder.or(namePredicate, descriptionPredicate, categoryPredicate);
    };
  }

  public static Specification<Product> hasName(String name) {
    return (root, query, criteriaBuilder) -> {
      if (name == null || name.trim().isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("name")),
          "%" + name.toLowerCase() + "%");
    };
  }

  public static Specification<Product> hasCategory(String category) {
    return (root, query, criteriaBuilder) -> {
      if (category == null || category.trim().isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(
          criteriaBuilder.lower(root.get("category")),
          category.toLowerCase());
    };
  }

  public static Specification<Product> priceBetween(Double minPrice, Double maxPrice) {
    return (root, query, criteriaBuilder) -> {
      if (minPrice == null && maxPrice == null) {
        return criteriaBuilder.conjunction();
      }

      if (minPrice != null && maxPrice != null) {
        return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
      } else if (minPrice != null) {
        return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
      } else {
        return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
      }
    };
  }

  public static Specification<Product> stockGreaterThan(Integer minStock) {
    return (root, query, criteriaBuilder) -> {
      if (minStock == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.greaterThan(root.get("stockQuantity"), minStock);
    };
  }
}
