package br.com.hexagonal.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class FaturaTest {

  private Fatura fatura() {
    return Fatura.criar(1L, new BigDecimal("150.00"));
  }

  @Test
  void criaFaturaValida() {
    Fatura fatura = fatura();
    assertNotNull(fatura.getId());
    assertEquals(1L, fatura.getPedidoId());
  }

  @Test
  void faturaComValorZeroNaoExiste() {
    assertThrows(RuntimeException.class, () -> Fatura.criar(1L, BigDecimal.ZERO));
  }

  @Test
  void faturaPrecisaDePedido() {
    assertThrows(RuntimeException.class, () -> Fatura.criar(null, BigDecimal.TEN));
    assertThrows(RuntimeException.class, () -> Fatura.criar(0L, BigDecimal.TEN));
  }

  @Test
  void guardaCodigoDeBarrasDoBoleto() {
    Fatura fatura = fatura();
    fatura.inserirBoleto("34191000000000000000000000000000000000000000");
    assertEquals("34191000000000000000000000000000000000000000", fatura.getCodigoBarras());
  }

  @Test
  void boletoSemCodigoDeBarrasEhRecusado() {
    Fatura fatura = fatura();
    assertThrows(RuntimeException.class, () -> fatura.inserirBoleto(null));
    assertThrows(RuntimeException.class, () -> fatura.inserirBoleto(" "));
  }

  @Test
  void chaveDeAcessoTemQuarentaEQuatroDigitos() {
    Fatura fatura = fatura();
    String chave = "1".repeat(44);
    fatura.inserirNotaFiscal(chave);
    assertEquals(chave, fatura.getChaveAcesso());
  }

  @Test
  void chaveDeAcessoInvalidaEhRecusada() {
    Fatura fatura = fatura();
    assertThrows(RuntimeException.class, () -> fatura.inserirNotaFiscal(null));
    assertThrows(RuntimeException.class, () -> fatura.inserirNotaFiscal("1".repeat(43)));
    assertThrows(RuntimeException.class, () -> fatura.inserirNotaFiscal("1".repeat(45)));
    assertThrows(RuntimeException.class, () -> fatura.inserirNotaFiscal("a".repeat(44)));
  }

  @Test
  void guardaHashDoEmail() {
    Fatura fatura = fatura();
    fatura.inserirHashEmail("abc123");
    assertEquals("abc123", fatura.getHashEmail());
    assertThrows(RuntimeException.class, () -> fatura.inserirHashEmail(""));
  }
}
