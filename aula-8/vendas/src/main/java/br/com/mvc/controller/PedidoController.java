package br.com.mvc.controller;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.model.Pedido;
import br.com.mvc.model.PedidoStatus;
import br.com.mvc.model.ProductStatus;
import br.com.mvc.service.PedidoService;
import br.com.mvc.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

  private final PedidoService service;
  private final ProductService productService;

  public PedidoController(PedidoService service, ProductService productService) {
    this.service = service;
    this.productService = productService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("pedidos", service.read());
    model.addAttribute("disponiveis", productService.read().stream()
        .filter(p -> p.getStatus() == ProductStatus.DISPONIVEL)
        .toList());
    return "pedidos";
  }

  @PostMapping
  public String fechar(@RequestParam("productId") Long productId, @RequestParam("quantidade") int quantidade,
      RedirectAttributes redirect) {
    try {
      mensagem(service.fechar(productId, quantidade), redirect);
    } catch (RegraDeNegocioException e) {
      redirect.addFlashAttribute("erro", e.getMessage());
    }
    return "redirect:/pedidos";
  }

  @PostMapping("/{id}/refaturar")
  public String refaturar(@PathVariable("id") Long id, RedirectAttributes redirect) {
    try {
      mensagem(service.refaturar(id), redirect);
    } catch (RegraDeNegocioException e) {
      redirect.addFlashAttribute("erro", e.getMessage());
    }
    return "redirect:/pedidos";
  }

  private void mensagem(Pedido pedido, RedirectAttributes redirect) {
    if (pedido.getStatus() == PedidoStatus.FATURADO) {
      redirect.addFlashAttribute("sucesso",
          "Pedido " + pedido.getId() + " fechado e faturado. Fatura " + pedido.getFaturaId() + ".");
    } else {
      redirect.addFlashAttribute("erro", "Pedido " + pedido.getId()
          + " fechado, mas não foi faturado: " + pedido.getErroFaturamento());
    }
  }
}
