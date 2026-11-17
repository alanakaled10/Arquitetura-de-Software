package br.com.mvc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Product product;

  private int quantidade;

  private BigDecimal valor;

  private LocalDateTime fechadoEm;

  @Enumerated(EnumType.STRING)
  private PedidoStatus status;

  // ID da fatura devolvido pelo hub de faturamento (aula 7).
  private String faturaId;

  // Última mensagem de erro da integração, exibida na tela para o vendedor.
  private String erroFaturamento;
}
