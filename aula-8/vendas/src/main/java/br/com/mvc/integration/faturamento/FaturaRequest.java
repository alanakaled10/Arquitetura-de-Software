package br.com.mvc.integration.faturamento;

import java.math.BigDecimal;

// Corpo do POST /fatura do hub (mesmo formato de FaturaPostRequest da aula 7).
public record FaturaRequest(Long pedidoId, BigDecimal valor) {
}
