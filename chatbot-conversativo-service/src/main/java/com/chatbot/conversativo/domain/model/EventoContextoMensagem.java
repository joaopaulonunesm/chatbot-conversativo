package com.chatbot.conversativo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventoContextoMensagem {

    CONTEXTO_MENSAGEM_CRIADO("CONTEXTO_MENSAGEM_CRIADO"),
    MENSAGEM_INCLUIDA_NO_CONTEXTO("MENSAGEM_INCLUIDA_NO_CONTEXTO");

    private String codigoEvento;
}
