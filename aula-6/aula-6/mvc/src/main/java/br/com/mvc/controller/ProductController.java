package br.com.mvc.controller;

import br.com.mvc.exception.RegraDeNegocioException;
import br.com.mvc.config.PerfilAdvice;
import br.com.mvc.service.ProductService;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("products", service.read());
    return "products";
  }

  @GetMapping("/new")
  public String form() {
    return "product-form";
  }

  @PostMapping
  public String create(@RequestParam("name") String name, @RequestParam("price") BigDecimal price, RedirectAttributes redirect) {
    try {
      service.create(name, price);
      redirect.addFlashAttribute("sucesso", "Produto cadastrado como INDISPONÍVEL.");
      return "redirect:/products";
    } catch (RegraDeNegocioException e) {
      redirect.addFlashAttribute("erro", e.getMessage());
      return "redirect:/products/new";
    }
  }

  @PostMapping("/{id}/descontinuar")
  public String descontinuar(@PathVariable("id") Long id, HttpSession session,
      RedirectAttributes redirect) {
    try {
      service.descontinuar(id, PerfilAdvice.perfilDe(session));
      redirect.addFlashAttribute("sucesso", "Produto descontinuado.");
    } catch (RegraDeNegocioException e) {
      redirect.addFlashAttribute("erro", e.getMessage());
    }
    return "redirect:/products";
  }
}
