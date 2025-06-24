package com.chatbot.conversativo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SituacaoContextoMensagem {

    ABERTO("ABERTO"),
    EXPIRADO("EXPIRADO"),
    PROCESSADO("PROCESSADO");

    private String codigo;

    public static SituacaoContextoMensagem valueByCodigo(String codigoSituacaoContextoMensagem) {
        return Arrays.stream(values())
                .filter(situacao -> situacao.getCodigo().equals(codigoSituacaoContextoMensagem))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Codigo situação contexto mensagem não existente"));
    }
}
