package br.com.techsolucoes.ControleEstoque.DTO;

import br.com.techsolucoes.ControleEstoque.entity.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimentacaoEstoqueRequestDTO {
    @NotNull(message = "O produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "O usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidade;

    @NotNull(message = "O tipo de movimentação é obrigatório")
    private TipoMovimentacao tipoMovimentacao;

    private String motivo;
}
