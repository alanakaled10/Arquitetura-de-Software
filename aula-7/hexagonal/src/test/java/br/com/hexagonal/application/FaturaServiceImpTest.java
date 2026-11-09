package br.com.hexagonal.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.hexagonal.domain.entities.Fatura;

// Sem Spring e sem tecnologia: o núcleo é testado só com adaptadores falsos nas portas de saída.
class FaturaServiceImpTest {

  private final List<String> eventos = new ArrayList<>();
  private final List<Fatura> salvas = new ArrayList<>();

  private FaturaServiceImp servico() {
    return new FaturaServiceImp(
        fatura -> {
          eventos.add("salvar");
          salvas.add(fatura);
        },
        fatura -> {
          eventos.add("boleto");
          return "BARRAS-" + fatura.getId();
        },
        fatura -> {
          eventos.add("nota");
          return "9".repeat(44);
        },
        fatura -> {
          eventos.add("email");
          return "hash-do-email";
        });
  }

  @Test
  void devolveOIdDaFaturaCriada() {
    UUID id = servico().faturarPedido(10L, new BigDecimal("200.00"));
    assertNotNull(id);
    assertEquals(id, salvas.get(0).getId());
  }

  @Test
  void persisteAFaturaAntesDeAcionarOsSistemasExternos() {
    servico().faturarPedido(10L, new BigDecimal("200.00"));
    assertEquals("salvar", eventos.get(0));
  }

  @Test
  void emiteBoletoNotaEEmailNessaOrdemEPersisteNoFim() {
    servico().faturarPedido(10L, new BigDecimal("200.00"));
    assertEquals(List.of("salvar", "boleto", "nota", "email", "salvar"), eventos);
  }

  @Test
  void guardaNaFaturaCodigoDeBarrasChaveDeAcessoEHash() {
    servico().faturarPedido(10L, new BigDecimal("200.00"));
    Fatura salva = salvas.get(salvas.size() - 1);
    assertTrue(salva.getCodigoBarras().startsWith("BARRAS-"));
    assertEquals(44, salva.getChaveAcesso().length());
    assertEquals("hash-do-email", salva.getHashEmail());
  }

  @Test
  void valorInvalidoNaoCriaNemPersisteFatura() {
    assertThrows(RuntimeException.class, () -> servico().faturarPedido(10L, BigDecimal.ZERO));
    assertTrue(eventos.isEmpty());
  }
}
