package br.com.mvc.integration.faturamento;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class FaturamentoClientTest {

  @Test
  void hubForaDoArViraFaturamentoException() {
    // Porta 1: ninguém escuta, a conexão é recusada.
    FaturamentoClient client = new FaturamentoClient(
        "http://localhost:1", Duration.ofMillis(500), Duration.ofMillis(500));

    assertThrows(FaturamentoException.class, () -> client.faturar(1L, new BigDecimal("10.00")));
  }
}
