package br.com.hexagonal.domain.ports.out.messaging;

import br.com.hexagonal.domain.entities.Fatura;

public interface BoletoMessaging {
  // Emite o boleto pela fila e devolve o código de barras.
  String emitirBoleto(Fatura fatura);
}
