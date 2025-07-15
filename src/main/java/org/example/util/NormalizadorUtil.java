package org.example.util;

import java.text.Normalizer;

public class NormalizadorUtil {

    public static String normalizar(String texto) {
        if (texto == null) {
            return null;
        }
        // 1. Normaliza para decompor os acentos
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // 2. Remove os acentos (diacríticos)
        textoNormalizado = textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // 3. Converte para minúsculas
        textoNormalizado = textoNormalizado.toLowerCase();
        // 4. Remove 's' do final para tratar plurais simples
        if (textoNormalizado.endsWith("s")) {
            textoNormalizado = textoNormalizado.substring(0, textoNormalizado.length() - 1);
        }
        return textoNormalizado;
    }
}

