package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.BaixaEstoquePedidoRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.MovimentacaoEstoqueResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.MovimentacaoEstoque;
import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.entity.TipoMovimentacao;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.exception.EstoqueInsuficienteException;
import br.com.techsolucoes.ControleEstoque.repository.MovimentacaoEstoqueRepository;
import br.com.techsolucoes.ControleEstoque.repository.ProdutoRepository;
import br.com.techsolucoes.ControleEstoque.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacaoEstoqueServiceTest {

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(movimentacaoEstoqueService, "usuarioIntegracaoId", 1L);
    }

    @Test
    void deveBaixarEstoquePorPedido() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Sistema")
                .perfil(Perfil.ADMIN)
                .build();

        Produto produto = Produto.builder()
                .id(10L)
                .nome("Produto")
                .codigo("P10")
                .quantidadeAtual(5)
                .build();

        BaixaEstoquePedidoRequestDTO request = new BaixaEstoquePedidoRequestDTO(
                100L,
                List.of(new BaixaEstoquePedidoRequestDTO.ItemDTO(10L, 2))
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(produtoRepository.findByIdComLock(10L)).thenReturn(Optional.of(produto));
        when(movimentacaoRepository.save(any(MovimentacaoEstoque.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<MovimentacaoEstoqueResponseDTO> response =
                movimentacaoEstoqueService.baixarEstoquePorPedido(request);

        assertEquals(3, produto.getQuantidadeAtual());
        assertEquals(1, response.size());
        assertEquals(TipoMovimentacao.SAIDA, response.get(0).tipoMovimentacao());
        assertEquals("Baixa automática do pedido 100", response.get(0).motivo());

        verify(produtoRepository).save(produto);
        verify(movimentacaoRepository).save(any(MovimentacaoEstoque.class));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueForInsuficiente() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Sistema")
                .perfil(Perfil.ADMIN)
                .build();

        Produto produto = Produto.builder()
                .id(10L)
                .nome("Produto")
                .codigo("P10")
                .quantidadeAtual(1)
                .build();

        BaixaEstoquePedidoRequestDTO request = new BaixaEstoquePedidoRequestDTO(
                100L,
                List.of(new BaixaEstoquePedidoRequestDTO.ItemDTO(10L, 2))
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(produtoRepository.findByIdComLock(10L)).thenReturn(Optional.of(produto));

        assertThrows(
                EstoqueInsuficienteException.class,
                () -> movimentacaoEstoqueService.baixarEstoquePorPedido(request)
        );

        assertEquals(1, produto.getQuantidadeAtual());
        verify(produtoRepository, never()).save(any());
        verify(movimentacaoRepository, never()).save(any());
    }
}
