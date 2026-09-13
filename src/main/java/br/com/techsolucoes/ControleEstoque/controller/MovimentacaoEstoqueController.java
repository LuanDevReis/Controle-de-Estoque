package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.MovimentacaoEstoqueRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.MovimentacaoEstoqueResponseDTO;
import br.com.techsolucoes.ControleEstoque.service.MovimentacaoEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Operation(summary = "Registrar movimentação de estoque (entrada ou saída)")
    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponseDTO> registrar(@Valid @RequestBody MovimentacaoEstoqueRequestDTO dto) {
        MovimentacaoEstoqueResponseDTO response = movimentacaoEstoqueService.registrarMovimentacao(dto);
        return ResponseEntity.ok(response);
    }
}
