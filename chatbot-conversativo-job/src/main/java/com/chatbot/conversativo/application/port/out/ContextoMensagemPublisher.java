package com.chatbot.conversativo.application.port.out;

import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.EventoContextoMensagem;

public interface ContextoMensagemPublisher {

    void notificar(ContextoMensagem contextoMensagem, EventoContextoMensagem eventoContextoMensagem);
}
