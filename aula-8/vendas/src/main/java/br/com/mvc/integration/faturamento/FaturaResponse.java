package br.com.mvc.integration.faturamento;

// Resposta do POST /fatura do hub (mesmo formato de FaturaPostResponse da aula 7).
public record FaturaResponse(String faturaId) {
}
