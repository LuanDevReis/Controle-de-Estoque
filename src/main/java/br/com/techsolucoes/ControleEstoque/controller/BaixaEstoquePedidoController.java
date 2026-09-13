package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.BaixaEstoquePedidoRequestDTO;
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

import java.util.List;

@RestController
@RequestMapping("/estoque/baixas/pedido")
@RequiredArgsConstructor
public class BaixaEstoquePedidoController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Operation(summary = "Baixar estoque automaticamente a partir de um pedido")
    @PostMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> baixarPorPedido(
            @Valid @RequestBody BaixaEstoquePedidoRequestDTO dto
    ) {
        List<MovimentacaoEstoqueResponseDTO> response = movimentacaoEstoqueService.baixarEstoquePorPedido(dto);
        return ResponseEntity.ok(response);
    }
}
