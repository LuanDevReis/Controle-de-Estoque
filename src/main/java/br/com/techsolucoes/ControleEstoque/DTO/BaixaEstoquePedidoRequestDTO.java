package br.com.techsolucoes.ControleEstoque.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BaixaEstoquePedidoRequestDTO(

        @NotNull(message = "O pedido é obrigatório")
        Long pedidoId,

        @NotEmpty(message = "A baixa deve possuir ao menos um item")
        @Valid
        List<ItemDTO> itens

) {
    public record ItemDTO(

            @NotNull(message = "O produto é obrigatório")
            Long produtoId,

            @NotNull(message = "A quantidade é obrigatória")
            @Min(value = 1, message = "A quantidade deve ser maior que zero")
            Integer quantidade

    ) {
    }
}
