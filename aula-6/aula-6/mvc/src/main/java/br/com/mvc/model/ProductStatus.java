package br.com.mvc.model;

public enum ProductStatus {
  DISPONIVEL("Disponível"),
  INDISPONIVEL("Indisponível"),
  DESCONTINUADO("Descontinuado");

  private final String descricao;

  ProductStatus(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
