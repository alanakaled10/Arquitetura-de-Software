package br.com.hexagonal.infra.adapters.out;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

// Apoio dos adaptadores simulados: gera valores determinísticos a partir de uma semente.
public final class Simulacao {

  private Simulacao() {
  }

  public static String sha256(String texto) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(texto.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 indisponível", e);
    }
  }

  // Sequência numérica com exatamente 'tamanho' dígitos.
  public static String digitos(String semente, int tamanho) {
    byte[] hash = HexFormat.of().parseHex(sha256(semente));
    String decimal = new BigInteger(1, hash).toString();
    // SHA-256 em decimal tem cerca de 77 dígitos, suficiente para 44.
    return decimal.length() >= tamanho
        ? decimal.substring(0, tamanho)
        : "0".repeat(tamanho - decimal.length()) + decimal;
  }
}
