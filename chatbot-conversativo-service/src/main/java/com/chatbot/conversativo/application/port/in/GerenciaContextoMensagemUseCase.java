package com.chatbot.conversativo.application.port.in;

import com.chatbot.conversativo.application.dto.ContextoMensagemInput;
import com.chatbot.conversativo.domain.model.ContextoMensagem;

import java.util.List;

public interface GerenciaContextoMensagemUseCase {
    void receberNovaMensagem(ContextoMensagemInput mensagemDTO);

    List<ContextoMensagem> buscarPorNumeroTelefone(String identificador);
}
