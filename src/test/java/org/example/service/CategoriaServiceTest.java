package org.example.service;

import org.example.dto.CategoriaRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Categoria;
import org.example.repository.CategoriaRepository;
import org.example.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Teste de unidade para a CategoriaService.
 * A anotação @ExtendWith(MockitoExtension.class) inicializa os mocks do Mockito.
 * Testamos a lógica de negócio do serviço de forma isolada, mockando as dependências (repositórios).
 */
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        // Objeto base para os testes
        categoria = new Categoria("Eletrônicos");
        categoria.setId(1L);
    }

    @Test
    void deveCriarCategoriaComSucesso() {
        // Arrange
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNome("Eletrônicos");

        // Simula que não existe categoria com nome normalizado "eletronicos"
        given(categoriaRepository.findByNomeNormalizado(anyString())).willReturn(Optional.empty());
        // Simula a ação de salvar, retornando a categoria com ID
        given(categoriaRepository.save(any(Categoria.class))).willReturn(categoria);

        // Act
        Categoria categoriaCriada = categoriaService.criarCategoria(dto);

        // Assert
        assertNotNull(categoriaCriada);
        assertEquals("Eletrônicos", categoriaCriada.getNome());
        // Verifica se o método save do repositório foi chamado exatamente uma vez.
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void naoDeveCriarCategoriaComNomeDuplicado() {
        given(categoriaRepository.findByNomeNormalizado(anyString())).willReturn(Optional.of(categoria));
        BusinessRuleException e = assertThrows(BusinessRuleException.class, () -> categoriaService.criarCategoria(new CategoriaRequestDTO("Eletrônicos")));
        assertEquals("Uma categoria com nome similar já existe: " + categoria.getNome(), e.getMessage());
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void deveAtualizarCategoriaComSucesso() {
        // Arrange
        Long categoriaId = 1L;
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNome("Livros Técnicos");

        // Simula a busca da categoria a ser atualizada
        given(categoriaRepository.findById(categoriaId)).willReturn(Optional.of(categoria));
        // Simula que não há conflito com outro nome de categoria
        given(categoriaRepository.findByNomeNormalizado("livros tecnicos")).willReturn(Optional.empty());
        // Simula a ação de salvar a categoria atualizada
        given(categoriaRepository.save(any(Categoria.class))).willAnswer(invocation -> invocation.getArgument(0));


        // Act
        Categoria categoriaAtualizada = categoriaService.atualizarCategoria(categoriaId, dto);

        // Assert
        assertEquals("Livros Técnicos", categoriaAtualizada.getNome());
        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    void naoDeveAtualizarCategoriaInexistente() {
        // Arrange
        Long idInexistente = 999L;
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNome("Qualquer Nome");

        // Simula que a categoria não foi encontrada
        given(categoriaRepository.findById(idInexistente)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.atualizarCategoria(idInexistente, dto);
        });
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void naoDeveAtualizarCategoriaParaNomeJaEmUso() {
        Categoria outraCategoria = new Categoria("Móveis");
        outraCategoria.setId(2L);
        given(categoriaRepository.findById(2L)).willReturn(Optional.of(outraCategoria));
        given(categoriaRepository.findByNomeNormalizado("eletronicos")).willReturn(Optional.of(categoria));

        BusinessRuleException e = assertThrows(BusinessRuleException.class, () -> categoriaService.atualizarCategoria(2L, new CategoriaRequestDTO("Eletrônicos")));
        assertEquals("Uma categoria com nome similar já existe: " + categoria.getNome(), e.getMessage());
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void deveDeletarCategoriaComSucesso() {
        // Arrange
        Long categoriaId = 1L;
        // Simula a busca da categoria a ser deletada
        given(categoriaRepository.findById(categoriaId)).willReturn(Optional.of(categoria));
        // Simula que não há produtos usando esta categoria
        given(produtoRepository.countByCategoria(categoria)).willReturn(0L);
        // Configura o mock para não fazer nada quando o método deletar for chamado.
        doNothing().when(categoriaRepository).delete(categoria);

        // Act
        assertDoesNotThrow(() -> categoriaService.deletarCategoria(categoriaId));

        // Assert
        verify(categoriaRepository, times(1)).delete(categoria);
    }

    @Test
    void naoDeveDeletarCategoriaEmUso() {
        given(categoriaRepository.findById(1L)).willReturn(Optional.of(categoria));
        given(produtoRepository.countByCategoria(categoria)).willReturn(5L);
        BusinessRuleException e = assertThrows(BusinessRuleException.class, () -> categoriaService.deletarCategoria(1L));
        assertEquals("Não é possível excluir uma categoria que está sendo usada por produtos.", e.getMessage());
        verify(categoriaRepository, never()).delete(any());
    }

    @Test
    void naoDeveDeletarCategoriaInexistente() {
        // Arrange
        Long idInexistente = 999L;
        given(categoriaRepository.findById(idInexistente)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.deletarCategoria(idInexistente);
        });
        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }
}
