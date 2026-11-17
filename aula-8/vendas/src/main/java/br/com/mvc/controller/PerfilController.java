package br.com.mvc.controller;

import br.com.mvc.config.PerfilAdvice;
import br.com.mvc.model.Perfil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Simula o login: o projeto ainda não tem autenticação, então o perfil é escolhido na tela.
@Controller
@RequestMapping("/perfil")
public class PerfilController {

  @PostMapping
  public String trocar(@RequestParam("perfil") Perfil perfil, @RequestParam(name = "voltar", defaultValue = "/products") String voltar,
      HttpSession session) {
    session.setAttribute(PerfilAdvice.ATRIBUTO, perfil);
    return "redirect:" + (voltar.startsWith("/") && !voltar.startsWith("//") ? voltar : "/products");
  }
}
