package br.com.hexagonal.infra.adapters.out.email;

import org.springframework.stereotype.Component;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.email.NotificacaoEmail;
import br.com.hexagonal.infra.adapters.out.Simulacao;
import lombok.extern.slf4j.Slf4j;

// Adaptador simulado: no lugar do envio SMTP real, monta o conteúdo, registra e devolve o hash dele.
@Slf4j
@Component
public class NotificacaoEmailImp implements NotificacaoEmail {

  @Override
  public String notificarCliente(Fatura fatura) {
    String conteudo = "Fatura " + fatura.getId() + " do pedido " + fatura.getPedidoId()
        + "\nValor: " + fatura.getValor()
        + "\nBoleto (código de barras): " + fatura.getCodigoBarras()
        + "\nNota fiscal (chave de acesso): " + fatura.getChaveAcesso();
    String hash = Simulacao.sha256(conteudo);
    log.info("[SMTP] E-mail enviado ao cliente do pedido {}, hash do conteúdo {}",
        fatura.getPedidoId(), hash);
    return hash;
  }
}
