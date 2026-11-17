package br.com.mvc.model;

public enum PedidoStatus {
  FATURADO("Faturado"),
  FALHA_FATURAMENTO("Falha no faturamento");

  private final String descricao;

  PedidoStatus(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
