package br.com.hexagonal.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Fatura {
  private UUID id;
  private Long pedidoId;
  private BigDecimal valor;
  private LocalDateTime emitidoEm;
  private String codigoBarras;
  private String chaveAcesso;
  private String hashEmail;

  @Builder 
  private Fatura(
    UUID id,
    Long pedidoId,
    BigDecimal valor,
    LocalDateTime emitidoEm,
    String codigoBarras,
    String chaveAcesso,
    String hashEmail
  ) {

    if (pedidoId == null || pedidoId <= 0) {
      throw new RuntimeException("A fatura precisa de um pedido!");
    }

    if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
      throw new RuntimeException("O valor da fatura deve ser maior que zero!");
    }

    this.id = id; 
    this.pedidoId = pedidoId; 
    this.valor = valor; 
    this.emitidoEm = emitidoEm; 
    this.codigoBarras = codigoBarras; 
    this.chaveAcesso = chaveAcesso; 
    this.hashEmail = hashEmail;
  }

  public static Fatura criar(Long pedidoId, BigDecimal valor) {
    return Fatura.builder()
      .id(UUID.randomUUID())
      .pedidoId(pedidoId)
      .valor(valor)
      .emitidoEm(LocalDateTime.now())
      .build();
  }

  // RF02: o boleto emitido gera um código de barras, guardado na fatura.
  public void inserirBoleto(String codigoBarras) {
    if (codigoBarras == null || codigoBarras.isBlank()) {
      throw new RuntimeException("O boleto emitido deve gerar um código de barras!");
    }
    this.codigoBarras = codigoBarras;
  }

  // RF03: a nota emitida gera uma chave de acesso de 44 dígitos, guardada na fatura.
  public void inserirNotaFiscal(String chaveAcesso) {
    if (chaveAcesso == null || !chaveAcesso.matches("\\d{44}")) {
      throw new RuntimeException("A chave de acesso da nota fiscal deve ter 44 dígitos!");
    }
    this.chaveAcesso = chaveAcesso;
  }

  // RF04: o e-mail enviado gera um hash do conteúdo, guardado na fatura.
  public void inserirHashEmail(String hashEmail) {
    if (hashEmail == null || hashEmail.isBlank()) {
      throw new RuntimeException("O e-mail enviado deve gerar um hash do conteúdo!");
    }
    this.hashEmail = hashEmail;
  }

}
