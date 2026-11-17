package br.com.hexagonal.infra.adapters.out.persistence;

import org.springframework.stereotype.Repository;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.persistence.FaturaRepository;
import br.com.hexagonal.infra.adapters.out.persistence.jpa.FaturaEntityJpa;
import br.com.hexagonal.infra.adapters.out.persistence.jpa.FaturaRepositoryJpa;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FaturaRepositoryImp implements FaturaRepository {

  private final FaturaRepositoryJpa repositoryJpa;

  @Override
  public void salvar(Fatura fatura) {
    repositoryJpa.save(FaturaEntityJpa.doDominio(fatura));
  }
  
}
