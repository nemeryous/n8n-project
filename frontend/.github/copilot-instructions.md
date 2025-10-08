# Hướng dẫn & Quy tắc cho Copilot - Dự án ReactJS

Tài liệu này đặt ra các quy tắc và tiêu chuẩn mà Copilot phải tuân thủ nghiêm ngặt khi tạo hoặc sửa đổi mã nguồn cho dự án này.  
Mục tiêu: **Đảm bảo mã nguồn nhất quán, hiệu suất cao và dễ bảo trì.**

---

## 1. Nguyên tắc cốt lõi & Tiêu chuẩn Code

Ưu tiên hàng đầu là tuân thủ **SOLID**, **DRY** và **KISS**.

### SOLID Principles

- **S - Single Responsibility Principle**:  
  Mỗi thành phần chỉ có một trách nhiệm duy nhất.
  - **Component**: chỉ hiển thị UI, xử lý sự kiện cơ bản. Logic phức tạp → tách ra custom hook hoặc Redux slice.
  - **Custom Hook**: đóng gói một logic tái sử dụng duy nhất.
  - **Redux Slice**: chỉ quản lý một phần state toàn cục (auth, cart...).

- **DRY (Don't Repeat Yourself)**:
  - JSX lặp lại → tách thành component con.
  - Logic lặp lại → tách thành custom hook hoặc hàm trong `src/lib`.

- **Composition over Inheritance**: Ưu tiên kết hợp nhiều component nhỏ.

- **Immutability**: State trong React/Redux là bất biến. Redux Toolkit + Immer đã hỗ trợ.

---

## 2. Cấu trúc Thư mục (Feature-Sliced)

```bash
/src
|-- /app                # Redux store, RTK Query API
|   |-- store.js
|   |-- api.js
|
|-- /components         # UI components tái sử dụng
|   |-- /ui             # Atomic: Button.jsx, Input.jsx, Card.jsx
|   |-- /layout         # Layout: Header.jsx, Sidebar.jsx, Footer.jsx
|
|-- /features           # Logic + UI cho từng tính năng
|   |-- /auth
|   |   |-- authSlice.js
|   |   |-- authApi.js
|   |   |-- Login.jsx
|   |   |-- Register.jsx
|   |
|   |-- /products
|       |-- productSlice.js
|       |-- productApi.js
|       |-- ProductList.jsx
|       |-- ProductDetails.jsx
|
|-- /hooks              # Custom hooks toàn cục
|   |-- useDebounce.js
|
|-- /lib                # Utils & constants
|   |-- utils.js
|   |-- constants.js
|
|-- /pages              # Route-level components
|   |-- HomePage.jsx
|   |-- LoginPage.jsx
|   |-- ProductDetailPage.jsx
|
|-- /routes             # React Router config
|   |-- index.jsx
|
|-- index.css           # Global Tailwind styles
|-- main.jsx            # Entry point
```

---

## 3. Quy tắc Đặt tên

- **Components & Pages**: PascalCase.jsx (VD: UserProfile.jsx, HomePage.jsx)
- **Custom Hooks**: useCamelCase.js (VD: useAuthStatus.js)
- **Redux Slices**: featureSlice.js (VD: authSlice.js)
- **RTK Query APIs**: featureApi.js (VD: productApi.js)
- **Hàm Utility**: camelCase.js trong lib

---

## 4. Quản lý State & Lấy Dữ liệu

- **Redux Toolkit = Single Source of Truth.**
- **RTK Query = DUY NHẤT để fetching/caching server state.**
- **TUYỆT ĐỐI KHÔNG dùng useEffect + fetch/axios.**
- **API endpoint → định nghĩa trong ...Api.js bằng createApi.**
- **Luôn xử lý isLoading, isError, isSuccess.**
- **UI State (modal, input…) → quản lý bằng useState hoặc useReducer trong component, không đưa vào Redux.**

---

## 5. Styling & Animation

- Tailwind CSS v4 là công cụ DUY NHẤT cho styling.
- KHÔNG viết CSS/SCSS riêng.
- KHÔNG dùng Styled Components hoặc CSS-in-JS.
- Viết utility class trực tiếp trong JSX.
- Theme global cấu hình qua src/index.css.

---

## 6. Animation:

- Framer Motion là thư viện DUY NHẤT.
- Dùng <motion.div> với initial, animate, exit, variants.
- Dùng AnimatePresence cho animation khi component bị xóa.

---

## 7. Routing

- React Router DOM quản lý routing.
- Cấu hình route tập trung ở src/routes/index.jsx với createBrowserRouter.
- Dùng các hooks (useNavigate, useParams, useLocation).
- Nested routes → <Outlet />.

---

## 8. Icons

- Font Awesome là thư viện icon chính thức.
- Luôn dùng <FontAwesomeIcon icon={...} />.
- Chỉ import icon cần thiết để giảm bundle size.

## 9. Xử lý Lỗi (Error Handling)

- **API Errors**: dùng isError và error từ RTK Query để hiển thị thông báo thân thiện.
- **Render Errors**: dùng Error Boundary (bọc quanh <Outlet /> hoặc vùng có nguy cơ crash).
