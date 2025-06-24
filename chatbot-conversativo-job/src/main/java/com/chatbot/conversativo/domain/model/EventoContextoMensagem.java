package com.chatbot.conversativo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventoContextoMensagem {

    CONTEXTO_MENSAGEM_EXPIRADO("CONTEXTO_MENSAGEM_EXPIRADO"),
    CONTEXTO_MENSAGEM_PROCESSADO("CONTEXTO_MENSAGEM_PROCESSADO");

    private String codigoEvento;
}
