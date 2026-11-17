package br.com.mvc.integration.faturamento;

// Qualquer falha na chamada ao hub: indisponível, lento demais, erro HTTP ou resposta inválida.
public class FaturamentoException extends RuntimeException {

  public FaturamentoException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }

  public FaturamentoException(String mensagem) {
    super(mensagem);
  }
}
