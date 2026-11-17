package br.com.mvc.service;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.integration.faturamento.FaturamentoClient;
import br.com.mvc.integration.faturamento.FaturamentoException;
import br.com.mvc.model.Pedido;
import br.com.mvc.model.PedidoStatus;
import br.com.mvc.model.Product;
import br.com.mvc.repository.PedidoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

  private final PedidoRepository repository;
  private final ProductService productService;
  private final EstoqueService estoqueService;
  private final FaturamentoClient faturamentoClient;

  public PedidoService(PedidoRepository repository, ProductService productService,
      EstoqueService estoqueService, FaturamentoClient faturamentoClient) {
    this.repository = repository;
    this.productService = productService;
    this.estoqueService = estoqueService;
    this.faturamentoClient = faturamentoClient;
  }

  @Transactional(readOnly = true)
  public List<Pedido> read() {
    return repository.findAllByOrderByIdDesc();
  }

  // Fecha o pedido: baixa o estoque, grava o pedido e chama o hub para faturar.
  @Transactional
  public Pedido fechar(Long productId, int quantidade) {
    Product product = productService.findById(productId);
    // Reaproveita as regras da aula 6 (quantidade, estoque suficiente, descontinuado).
    estoqueService.saida(productId, quantidade);

    Pedido pedido = new Pedido();
    pedido.setProduct(product);
    pedido.setQuantidade(quantidade);
    pedido.setValor(product.getPrice().multiply(BigDecimal.valueOf(quantidade)));
    pedido.setFechadoEm(LocalDateTime.now());
    // O pedido é gravado antes da chamada: o hub precisa do ID e a venda não se perde se ele falhar.
    repository.save(pedido);

    return faturar(pedido);
  }

  // Nova tentativa manual para pedidos que ficaram sem fatura.
  @Transactional
  public Pedido refaturar(Long pedidoId) {
    Pedido pedido = repository.findById(pedidoId)
        .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado."));
    if (pedido.getStatus() == PedidoStatus.FATURADO) {
      throw new RegraDeNegocioException("Este pedido já foi faturado.");
    }
    return faturar(pedido);
  }

  private Pedido faturar(Pedido pedido) {
    try {
      String faturaId = faturamentoClient.faturar(pedido.getId(), pedido.getValor());
      pedido.setFaturaId(faturaId);
      pedido.setStatus(PedidoStatus.FATURADO);
      pedido.setErroFaturamento(null);
    } catch (FaturamentoException e) {
      // A venda continua registrada; fica marcada para nova tentativa.
      pedido.setStatus(PedidoStatus.FALHA_FATURAMENTO);
      pedido.setErroFaturamento(e.getMessage());
    }
    return repository.save(pedido);
  }
}
