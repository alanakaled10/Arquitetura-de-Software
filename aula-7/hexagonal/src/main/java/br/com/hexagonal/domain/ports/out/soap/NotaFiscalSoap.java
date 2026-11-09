package br.com.hexagonal.domain.ports.out.soap;

import br.com.hexagonal.domain.entities.Fatura;

public interface NotaFiscalSoap {
  // Emite a nota fiscal e devolve a chave de acesso (44 dígitos).
  String emitirNota(Fatura fatura);
}
