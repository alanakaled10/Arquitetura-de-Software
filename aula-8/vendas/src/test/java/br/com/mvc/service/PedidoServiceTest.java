package br.com.mvc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.integration.faturamento.FaturamentoClient;
import br.com.mvc.integration.faturamento.FaturamentoException;
import br.com.mvc.model.Pedido;
import br.com.mvc.model.PedidoStatus;
import br.com.mvc.model.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PedidoServiceTest {

  @Autowired
  ProductService productService;

  @Autowired
  EstoqueService estoqueService;

  @Autowired
  PedidoService pedidoService;

  // O hub é simulado: os testes não dependem do projeto da aula 7 estar no ar.
  @MockitoBean
  FaturamentoClient faturamentoClient;

  private Long produtoComEstoque(int quantidade) {
    Product p = productService.create("Mouse", new BigDecimal("50.00"));
    estoqueService.entrada(p.getId(), quantidade);
    return p.getId();
  }

  @Test
  void fecharPedidoGuardaIdDaFaturaDevolvidoPeloHub() {
    Long productId = produtoComEstoque(10);
    when(faturamentoClient.faturar(any(), any())).thenReturn("fatura-123");

    Pedido pedido = pedidoService.fechar(productId, 3);

    assertEquals(PedidoStatus.FATURADO, pedido.getStatus());
    assertEquals("fatura-123", pedido.getFaturaId());
    assertEquals(new BigDecimal("150.00"), pedido.getValor());
    assertEquals(7, productService.findById(productId).getStock());
    verify(faturamentoClient).faturar(eq(pedido.getId()), eq(new BigDecimal("150.00")));
  }

  @Test
  void falhaNoHubMantemPedidoRegistradoSemFatura() {
    Long productId = produtoComEstoque(10);
    when(faturamentoClient.faturar(any(), any()))
        .thenThrow(new FaturamentoException("Hub de faturamento indisponível."));

    Pedido pedido = pedidoService.fechar(productId, 2);

    assertNotNull(pedido.getId());
    assertEquals(PedidoStatus.FALHA_FATURAMENTO, pedido.getStatus());
    assertNull(pedido.getFaturaId());
    assertEquals("Hub de faturamento indisponível.", pedido.getErroFaturamento());
  }

  @Test
  void refaturarDepoisDaFalhaGuardaFatura() {
    Long productId = produtoComEstoque(10);
    when(faturamentoClient.faturar(any(), any()))
        .thenThrow(new FaturamentoException("Hub de faturamento indisponível."))
        .thenReturn("fatura-456");

    Pedido pedido = pedidoService.fechar(productId, 1);
    Pedido refaturado = pedidoService.refaturar(pedido.getId());

    assertEquals(PedidoStatus.FATURADO, refaturado.getStatus());
    assertEquals("fatura-456", refaturado.getFaturaId());
    assertNull(refaturado.getErroFaturamento());
  }

  @Test
  void naoRefaturaPedidoJaFaturado() {
    Long productId = produtoComEstoque(10);
    when(faturamentoClient.faturar(any(), any())).thenReturn("fatura-789");
    Pedido pedido = pedidoService.fechar(productId, 1);

    assertThrows(RegraDeNegocioException.class, () -> pedidoService.refaturar(pedido.getId()));
  }

  @Test
  void estoqueInsuficienteNaoChamaOHub() {
    Long productId = produtoComEstoque(1);

    assertThrows(RegraDeNegocioException.class, () -> pedidoService.fechar(productId, 5));
    verify(faturamentoClient, never()).faturar(any(), any());
  }
}
