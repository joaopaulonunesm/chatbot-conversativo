package com.chatbot.conversativo.application.port.out;

import com.chatbot.conversativo.domain.model.Mensagem;

import java.util.List;

public interface MensagemRepository {

    void salvar(String identificador, Mensagem mensagem);

    List<Mensagem> buscarMensagensPorIdentificador(String identificador);
}
