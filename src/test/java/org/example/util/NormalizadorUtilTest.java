package org.example.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NormalizadorUtilTest {

    @Test
    void deveConverterParaMinusculas() {
        assertEquals("teste", NormalizadorUtil.normalizar("TESTE"));
    }

    @Test
    void deveRemoverAcentos() {
        assertEquals("eletronico", NormalizadorUtil.normalizar("eletrônico"));
    }

    @Test
    void deveRemoverPluralSimples() {
        assertEquals("categoria", NormalizadorUtil.normalizar("categorias"));
    }

    @Test
    void deveAplicarTodasAsRegras() {
        assertEquals("eletronico", NormalizadorUtil.normalizar("Eletrônicos"));
    }

    @Test
    void deveRetornarNuloParaEntradaNula() {
        assertNull(NormalizadorUtil.normalizar(null));
    }

    @Test
    void deveRetornarStringVaziaParaEntradaVazia() {
        assertEquals("", NormalizadorUtil.normalizar(""));
    }
}

