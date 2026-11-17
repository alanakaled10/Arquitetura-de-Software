package br.com.hexagonal.domain.ports.out.email;

import br.com.hexagonal.domain.entities.Fatura;

public interface NotificacaoEmail {
  // Envia o e-mail ao cliente e devolve o hash do conteúdo enviado.
  String notificarCliente(Fatura fatura);
}
