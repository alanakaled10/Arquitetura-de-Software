package br.com.mvc.repository;

import br.com.mvc.model.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

  List<Pedido> findAllByOrderByIdDesc();
}
