package br.com.mvc.service;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.model.Product;
import br.com.mvc.model.ProductStatus;
import br.com.mvc.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {

  private final ProductRepository repository;

  public EstoqueService(ProductRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public List<Product> read() {
    return repository.findAll();
  }

  @Transactional
  public Product entrada(Long productId, int quantidade) {
    Product product = buscarMovimentavel(productId);
    validarQuantidade(quantidade);

    product.setStock(product.getStock() + quantidade);
    // Saiu de zero: o produto passa a ficar disponível.
    if (product.getStock() > 0) {
      product.setStatus(ProductStatus.DISPONIVEL);
    }
    return repository.save(product);
  }

  @Transactional
  public Product saida(Long productId, int quantidade) {
    Product product = buscarMovimentavel(productId);
    validarQuantidade(quantidade);

    if (product.getStatus() == ProductStatus.INDISPONIVEL || product.getStock() == 0) {
      throw new RegraDeNegocioException("Produto sem estoque disponível.");
    }
    if (quantidade > product.getStock()) {
      throw new RegraDeNegocioException(
          "Estoque insuficiente: há " + product.getStock() + " unidade(s) disponível(is).");
    }

    product.setStock(product.getStock() - quantidade);
    // Zerou: o produto passa a ficar indisponível.
    if (product.getStock() == 0) {
      product.setStatus(ProductStatus.INDISPONIVEL);
    }
    return repository.save(product);
  }

  // Produto descontinuado não pode ser manipulado no estoque.
  private Product buscarMovimentavel(Long productId) {
    Product product = repository.findById(productId)
        .orElseThrow(() -> new RegraDeNegocioException("Produto não encontrado."));
    if (product.getStatus() == ProductStatus.DESCONTINUADO) {
      throw new RegraDeNegocioException("Produto descontinuado não pode ser movimentado no estoque.");
    }
    return product;
  }

  private void validarQuantidade(int quantidade) {
    if (quantidade <= 0) {
      throw new RegraDeNegocioException("A quantidade deve ser maior que zero.");
    }
  }
}
