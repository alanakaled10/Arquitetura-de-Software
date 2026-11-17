package br.com.hexagonal.infra.adapters.out.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.hexagonal.domain.entities.Fatura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table(name = "fatura")
public class FaturaEntityJpa {

  @Id 
  @Column(name = "id")
  private UUID id;
  
  @Column(name = "pedido_id")
  private Long pedidoId;
  
  @Column(name = "valor")
  private BigDecimal valor;
  
  @Column(name = "emitido_em")
  private LocalDateTime emitidoEm;
  
  @Column(name = "codigo_barras")
  private String codigoBarras;
  
  @Column(name = "chave_acesso")
  private String chaveAcesso;
  
  @Column(name = "hash_email")
  private String hashEmail;

    public static FaturaEntityJpa doDominio(Fatura fatura) {
    return FaturaEntityJpa.builder()
      .id(fatura.getId())
      .pedidoId(fatura.getPedidoId())
      .valor(fatura.getValor())
      .emitidoEm(fatura.getEmitidoEm())
      .codigoBarras(fatura.getCodigoBarras())
      .chaveAcesso(fatura.getChaveAcesso())
      .hashEmail(fatura.getHashEmail())
      .build();
  }
}
