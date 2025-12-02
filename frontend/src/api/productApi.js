import React, { useState, useEffect } from 'react';
import axios from 'axios';

function ProductList() {
    // 1. Khai báo State để lưu trữ dữ liệu sản phẩm
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // URL API
    const API_URL = 'http://localhost:8080/quan_ly_chi_tiet_san_pham';

    // 2. Hàm gọi API
    const fetchProducts = async () => {
        try {
            // Đặt loading = true trước khi gọi API
            setLoading(true); 
            
            // Thực hiện GET request
            const response = await axios.get(API_URL);
            
            // 3. Lưu dữ liệu vào State
            setProducts(response.data);
            
            // Xử lý thành công
            setLoading(false);
        } catch (err) {
            // Xử lý lỗi (ví dụ: BE chưa chạy, lỗi CORS,...)
            console.error("Lỗi khi fetch dữ liệu:", err);
            setError("Không thể tải dữ liệu sản phẩm. Vui lòng kiểm tra Server Backend.");
            setLoading(false);
        }
    };

    // 4. Sử dụng useEffect để gọi hàm fetchProducts khi component được mount
    useEffect(() => {
        fetchProducts();
    }, []); // Mảng dependencies rỗng: chỉ chạy một lần sau khi render đầu tiên

    // --- 5. Logic Hiển thị Giao diện ---

    if (loading) {
        return <div>Đang tải dữ liệu...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>Lỗi: {error}</div>;
    }

    return (
        <div className="product-list-container">
            <h2>Danh sách Chi Tiết Sản Phẩm</h2>
            <div className="product-grid">
                {/* 6. Lặp (Map) qua mảng products để hiển thị từng sản phẩm */}
                {products.map((product) => (
                    // Cần có thuộc tính 'key' duy nhất cho mỗi item trong list
                    <div key={product.id} className="product-card"> 
                        {/* Thay thế thuộc tính trong product.name, product.price,... bằng các thuộc tính thực tế trong Entity chiTietSanPham_Entity */}
                        <h3>{product.name || `Sản phẩm ID: ${product.id}`}</h3>
                        <p>Giá: {product.price} VND</p>
                        <p>Màu: {product.color}</p>
                        {/* Thẻ Image nếu có */}
                        {product.imageUrl && <img src={product.imageUrl} alt={product.name} style={{ width: '100px', height: '100px' }} />}
                    </div>
                ))}
            </div>
            {products.length === 0 && <div>Không có sản phẩm nào để hiển thị.</div>}
        </div>
    );
}

export default ProductList;