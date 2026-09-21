package br.com.mvc.model.repository;

import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mvc.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, BigInteger>{
    
}
