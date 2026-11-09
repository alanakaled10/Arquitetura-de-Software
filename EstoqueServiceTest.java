package br.com.mvc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.model.Perfil;
import br.com.mvc.model.Product;
import br.com.mvc.model.ProductStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class EstoqueServiceTest {

  @Autowired
  ProductService productService;

  @Autowired
  EstoqueService estoqueService;

  private Product novoProduto() {
    return productService.create("Teclado", new BigDecimal("99.90"));
  }

  @Test
  void produtoNasceIndisponivelComEstoqueZero() {
    Product p = novoProduto();
    assertEquals(ProductStatus.INDISPONIVEL, p.getStatus());
    assertEquals(0, p.getStock());
  }

  @Test
  void entradaQuandoSaiDeZeroTornaDisponivel() {
    Product p = estoqueService.entrada(novoProduto().getId(), 5);
    assertEquals(5, p.getStock());
    assertEquals(ProductStatus.DISPONIVEL, p.getStatus());
  }

  @Test
  void saidaParcialMantemDisponivel() {
    Long id = novoProduto().getId();
    estoqueService.entrada(id, 5);
    Product p = estoqueService.saida(id, 2);
    assertEquals(3, p.getStock());
    assertEquals(ProductStatus.DISPONIVEL, p.getStatus());
  }

  @Test
  void saidaQueZeraTornaIndisponivel() {
    Long id = novoProduto().getId();
    estoqueService.entrada(id, 5);
    Product p = estoqueService.saida(id, 5);
    assertEquals(0, p.getStock());
    assertEquals(ProductStatus.INDISPONIVEL, p.getStatus());
  }

  @Test
  void saidaSemEstoqueEhRecusada() {
    Long id = novoProduto().getId();
    assertThrows(RegraDeNegocioException.class, () -> estoqueService.saida(id, 1));
  }

  @Test
  void saidaMaiorQueEstoqueEhRecusadaESemAlterarEstoque() {
    Long id = novoProduto().getId();
    estoqueService.entrada(id, 3);
    assertThrows(RegraDeNegocioException.class, () -> estoqueService.saida(id, 4));
    assertEquals(3, productService.findById(id).getStock());
  }

  @Test
  void quantidadeInvalidaEhRecusada() {
    Long id = novoProduto().getId();
    assertThrows(RegraDeNegocioException.class, () -> estoqueService.entrada(id, 0));
    assertThrows(RegraDeNegocioException.class, () -> estoqueService.entrada(id, -1));
  }

  @Test
  void somenteAdmDescontinua() {
    Long id = novoProduto().getId();
    assertThrows(RegraDeNegocioException.class, () -> productService.descontinuar(id, Perfil.ESTOQUE));
    assertThrows(RegraDeNegocioException.class, () -> productService.descontinuar(id, Perfil.VENDEDOR));
    assertEquals(ProductStatus.DESCONTINUADO, productService.descontinuar(id, Perfil.ADM).getStatus());
  }

  @Test
  void produtoDescontinuadoNaoMovimentaEstoque() {
    Long id = novoProduto().getId();
    estoqueService.entrada(id, 5);
    productService.descontinuar(id, Perfil.ADM);
    assertThrows(RegraDeNegocioException.class, () -> estoqueService.entrada(id, 1));
    assertThrows(RegraDeNegocioException.class, () -> estoqueService.saida(id, 1));
  }
}
