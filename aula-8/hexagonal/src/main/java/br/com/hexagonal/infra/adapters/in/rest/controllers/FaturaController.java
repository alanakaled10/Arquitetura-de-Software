package br.com.hexagonal.infra.adapters.in.rest.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hexagonal.domain.ports.in.FaturaService;
import br.com.hexagonal.infra.adapters.in.rest.dtos.FaturaPostRequest;
import br.com.hexagonal.infra.adapters.in.rest.dtos.FaturaPostResponse;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/fatura")
@RequiredArgsConstructor
public class FaturaController {

  private final FaturaService faturaService;

  @PostMapping
  public ResponseEntity<FaturaPostResponse> post(
    @RequestBody FaturaPostRequest faturaPostRequest) {
      UUID faturaId = faturaService.faturarPedido(
        faturaPostRequest.pedidoId(), faturaPostRequest.valor());
      return ResponseEntity.status(201).body(new FaturaPostResponse(faturaId.toString()));
    }
  
}
