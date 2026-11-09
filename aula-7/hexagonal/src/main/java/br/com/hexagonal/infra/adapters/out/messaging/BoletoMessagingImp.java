package br.com.hexagonal.infra.adapters.out.messaging;

import org.springframework.stereotype.Component;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.messaging.BoletoMessaging;
import br.com.hexagonal.infra.adapters.out.Simulacao;
import lombok.extern.slf4j.Slf4j;

// Adaptador simulado: no lugar da fila real, apenas registra a publicação e a resposta do emissor.
@Slf4j
@Component
public class BoletoMessagingImp implements BoletoMessaging {

  @Override
  public String emitirBoleto(Fatura fatura) {
    log.info("[FILA DE PEDIDOS] Publicando pedido {} (fatura {}) para emissão do boleto",
        fatura.getPedidoId(), fatura.getId());
    String codigoBarras = Simulacao.digitos("boleto:" + fatura.getId(), 44);
    log.info("[FILA DE RETORNO] Boleto emitido, código de barras {}", codigoBarras);
    return codigoBarras;
  }
}
