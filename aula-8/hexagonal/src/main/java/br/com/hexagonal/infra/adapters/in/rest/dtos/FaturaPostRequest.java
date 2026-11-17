package br.com.hexagonal.infra.adapters.in.rest.dtos;

import java.math.BigDecimal;

public record FaturaPostRequest(Long pedidoId, BigDecimal valor) {
  
}
