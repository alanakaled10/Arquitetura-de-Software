package br.com.hexagonal.domain.ports.out.persistence;

import br.com.hexagonal.domain.entities.Fatura;

public interface FaturaRepository {
  void salvar(Fatura fatura);
}
