package br.com.hexagonal.infra.adapters.out;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.infra.adapters.out.email.NotificacaoEmailImp;
import br.com.hexagonal.infra.adapters.out.messaging.BoletoMessagingImp;
import br.com.hexagonal.infra.adapters.out.soap.NotaFiscalSoapImp;

class AdaptadoresSimuladosTest {

  private final Fatura fatura = Fatura.criar(7L, new BigDecimal("99.90"));

  @Test
  void boletoGeraCodigoDeBarras() {
    String codigo = new BoletoMessagingImp().emitirBoleto(fatura);
    assertTrue(codigo.matches("\\d{44}"));
  }

  @Test
  void notaGeraChaveDeAcessoDe44Digitos() {
    String chave = new NotaFiscalSoapImp().emitirNota(fatura);
    assertTrue(chave.matches("\\d{44}"));
  }

  @Test
  void emailGeraHashDoConteudo() {
    fatura.inserirBoleto(new BoletoMessagingImp().emitirBoleto(fatura));
    fatura.inserirNotaFiscal(new NotaFiscalSoapImp().emitirNota(fatura));
    String hash = new NotificacaoEmailImp().notificarCliente(fatura);
    assertTrue(hash.matches("[0-9a-f]{64}"));
    // mesmo conteúdo, mesmo hash
    assertEquals(hash, new NotificacaoEmailImp().notificarCliente(fatura));
  }

  @Test
  void conteudoDiferenteGeraHashDiferente() {
    Fatura outra = Fatura.criar(8L, new BigDecimal("10.00"));
    assertNotEquals(new NotificacaoEmailImp().notificarCliente(fatura),
        new NotificacaoEmailImp().notificarCliente(outra));
  }
}
