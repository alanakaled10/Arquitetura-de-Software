package br.com.hexagonal.application;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.in.FaturaService;
import br.com.hexagonal.domain.ports.out.email.NotificacaoEmail;
import br.com.hexagonal.domain.ports.out.messaging.BoletoMessaging;
import br.com.hexagonal.domain.ports.out.persistence.FaturaRepository;
import br.com.hexagonal.domain.ports.out.soap.NotaFiscalSoap;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class FaturaServiceImp implements FaturaService{

  private final FaturaRepository faturaRepository;
  private final BoletoMessaging boletoMessaging;
  private final NotaFiscalSoap notaFiscalSoap;
  private final NotificacaoEmail notificacaoEmail;

  @Override
  public UUID faturarPedido(Long pedidoId, BigDecimal valor) {
    // RF01: cria e persiste a fatura antes de acionar qualquer sistema externo.
    Fatura fatura = Fatura.criar(pedidoId, valor);
    faturaRepository.salvar(fatura);

    // RF02: boleto por fila.
    fatura.inserirBoleto(boletoMessaging.emitirBoleto(fatura));

    // RF03: nota fiscal por SOAP.
    fatura.inserirNotaFiscal(notaFiscalSoap.emitirNota(fatura));

    // RF04: e-mail por SMTP, por último, pois leva o boleto e a nota.
    fatura.inserirHashEmail(notificacaoEmail.notificarCliente(fatura));

    faturaRepository.salvar(fatura);
    return fatura.getId();
  }
  
}
