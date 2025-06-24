package com.chatbot.conversativo.application.port.out;

import com.chatbot.conversativo.domain.model.Mensagem;

import java.util.List;

public interface MensagemRepository {

    List<Mensagem> buscarMensagensPorIdentificador(String identificador);
}
