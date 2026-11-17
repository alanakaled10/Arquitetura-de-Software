package br.com.mvc.service;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.model.Perfil;
import br.com.mvc.model.Product;
import br.com.mvc.model.ProductStatus;
import br.com.mvc.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository repository;

  public ProductService(ProductRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public Product create(String name, BigDecimal price) {
    if (name == null || name.isBlank()) {
      throw new RegraDeNegocioException("Informe o nome do produto.");
    }
    if (price == null || price.signum() < 0) {
      throw new RegraDeNegocioException("Informe um valor válido.");
    }
    Product product = new Product();
    product.setName(name.trim());
    product.setPrice(price);
    // estoque 0 e status INDISPONIVEL já são os valores iniciais da entidade
    return repository.save(product);
  }

  @Transactional(readOnly = true)
  public List<Product> read() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Product findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new RegraDeNegocioException("Produto não encontrado."));
  }

  // Transição para DESCONTINUADO é exclusiva do administrador.
  @Transactional
  public Product descontinuar(Long id, Perfil perfil) {
    if (perfil != Perfil.ADM) {
      throw new RegraDeNegocioException("Somente o administrador pode descontinuar um produto.");
    }
    Product product = findById(id);
    product.setStatus(ProductStatus.DESCONTINUADO);
    return repository.save(product);
  }
}
