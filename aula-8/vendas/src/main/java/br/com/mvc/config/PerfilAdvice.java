package br.com.mvc.config;

import br.com.mvc.model.Perfil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// Disponibiliza o perfil atual (guardado na sessão) para todas as views.
@ControllerAdvice
public class PerfilAdvice {

  public static final String ATRIBUTO = "perfil";

  public static Perfil perfilDe(HttpSession session) {
    Object perfil = session.getAttribute(ATRIBUTO);
    return perfil instanceof Perfil p ? p : Perfil.VENDEDOR;
  }

  @ModelAttribute("perfilAtual")
  public Perfil perfilAtual(HttpSession session) {
    return perfilDe(session);
  }

  @ModelAttribute("perfis")
  public Perfil[] perfis() {
    return Perfil.values();
  }
}
