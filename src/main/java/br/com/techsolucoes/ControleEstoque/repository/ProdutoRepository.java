package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.entity.Categoria;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByNomeAndCategoriaAndFornecedor(String nome, Categoria categoria, Fornecedor fornecedor);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.id = :id")
    Optional<Produto> findByIdComLock(@Param("id") Long id);

    @Query(value = """
            select\s
            	p.id as ID,
            	p.nome as NOME,
                p.quantidade_atual as QTD_ATUAL,
                p.estoque_minimo as QTD_ESTOQUE_MINIMO
            from produto p
            where p.quantidade_atual <= p.estoque_minimo
            """, nativeQuery = true)
    List<Object[]> findProdutosComEstoqueBaixo();
}
