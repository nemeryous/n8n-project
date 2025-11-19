import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import ProductCard from "../ProductCard";

describe("ProductCard", () => {
  it("renders product information", () => {
    const mockProduct = {
      id: 1,
      name: "Test Product",
      price: 100000,
      stock_quantity: 10,
      image_url: "https://example.com/image.jpg",
    };

    const mockOnAddToCart = () => {};

    render(<ProductCard product={mockProduct} onAddToCart={mockOnAddToCart} />);

    expect(screen.getByText("Test Product")).toBeInTheDocument();
    expect(screen.getByText("100.000đ")).toBeInTheDocument();
  });
});

