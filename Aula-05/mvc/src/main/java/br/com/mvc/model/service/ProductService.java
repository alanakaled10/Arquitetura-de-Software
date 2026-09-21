package br.com.mvc.model.service;

import org.springframework.stereotype.Service;
import br.com.mvc.model.repository.ProductRepository;
import br.com.mvc.model.entity.Product;
import java.util.List;
import java.math.BigInteger;

@Service 
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> read() {
        return productRepository.findAll();
    }
}
