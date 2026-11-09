package br.com.hexagonal.infra.adapters.out.soap;

import org.springframework.stereotype.Component;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.soap.NotaFiscalSoap;
import br.com.hexagonal.infra.adapters.out.Simulacao;
import lombok.extern.slf4j.Slf4j;

// Adaptador simulado: no lugar da chamada SOAP real, apenas registra a requisição e a resposta.
@Slf4j
@Component
public class NotaFiscalSoapImp implements NotaFiscalSoap {

  @Override
  public String emitirNota(Fatura fatura) {
    log.info("[SOAP] Enviando requisição de emissão de nota da fatura {} (valor {})",
        fatura.getId(), fatura.getValor());
    String chaveAcesso = Simulacao.digitos("nota:" + fatura.getId(), 44);
    log.info("[SOAP] Nota emitida, chave de acesso {}", chaveAcesso);
    return chaveAcesso;
  }
}
