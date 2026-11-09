package br.com.mvc.controller;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.service.EstoqueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

  private final EstoqueService service;

  public EstoqueController(EstoqueService service) {
    this.service = service;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("products", service.read());
    return "estoque";
  }

  @PostMapping("/entrada")
  public String entrada(@RequestParam("productId") Long productId, @RequestParam("quantidade") int quantidade, RedirectAttributes redirect) {
    try {
      service.entrada(productId, quantidade);
      redirect.addFlashAttribute("sucesso", "Entrada registrada.");
    } catch (RegraDeNegocioException e) {
      redirect.addFlashAttribute("erro", e.getMessage());
    }
    return "redirect:/estoque";
  }

  @PostMapping("/saida")
  public String saida(@RequestParam("productId") Long productId, @RequestParam("quantidade") int quantidade, RedirectAttributes redirect) {
    try {
      service.saida(productId, quantidade);
      redirect.addFlashAttribute("sucesso", "Saída registrada.");
    } catch (RegraDeNegocioException e) {
      redirect.addFlashAttribute("erro", e.getMessage());
    }
    return "redirect:/estoque";
  }
}
