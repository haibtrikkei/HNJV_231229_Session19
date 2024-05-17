package ra.demo_thymeleaf.dao;

import ra.demo_thymeleaf.entity.Product;

import java.util.List;

public interface ProductDAO {
    public List<Product> getProducts();
    public boolean insertProduct(Product product);
    public Product getProductById(Integer proId);
    public boolean updateProduct(Product product);
}
