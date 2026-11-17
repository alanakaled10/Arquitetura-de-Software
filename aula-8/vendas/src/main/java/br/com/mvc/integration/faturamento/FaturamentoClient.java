package br.com.mvc.integration.faturamento;

import java.math.BigDecimal;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

// Cliente REST do hub de faturamento (projeto hexagonal da aula 7, porta 8081).
// Isola a integração: o resto da aplicação de vendas não conhece HTTP nem os DTOs do hub.
@Component
public class FaturamentoClient {

  private final RestClient restClient;

  public FaturamentoClient(
      @Value("${faturamento.url}") String url,
      @Value("${faturamento.timeout-conexao:2s}") Duration timeoutConexao,
      @Value("${faturamento.timeout-leitura:5s}") Duration timeoutLeitura) {
    // Sem timeout, uma chamada travada no hub prende a requisição do vendedor indefinidamente.
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(timeoutConexao);
    requestFactory.setReadTimeout(timeoutLeitura);
    this.restClient = RestClient.builder()
        .baseUrl(url)
        .requestFactory(requestFactory)
        .build();
  }

  public String faturar(Long pedidoId, BigDecimal valor) {
    try {
      FaturaResponse resposta = restClient.post()
          .uri("/fatura")
          .body(new FaturaRequest(pedidoId, valor))
          .retrieve()
          .body(FaturaResponse.class);

      if (resposta == null || resposta.faturaId() == null || resposta.faturaId().isBlank()) {
        throw new FaturamentoException("O hub de faturamento não devolveu o ID da fatura.");
      }
      return resposta.faturaId();
    } catch (ResourceAccessException e) {
      // Conexão recusada ou tempo esgotado.
      throw new FaturamentoException("Hub de faturamento indisponível ou sem resposta no tempo limite.", e);
    } catch (RestClientResponseException e) {
      throw new FaturamentoException(
          "Hub de faturamento respondeu com erro HTTP " + e.getStatusCode().value() + ".", e);
    } catch (RestClientException e) {
      throw new FaturamentoException("Falha na chamada ao hub de faturamento.", e);
    }
  }
}
