package com.chatbot.conversativo.application.port.out;

import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.Mensagem;
import com.chatbot.conversativo.domain.model.SituacaoContextoMensagem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContextoMensagemRepository {

    void salvar(ContextoMensagem contextoMensagem);

    Optional<ContextoMensagem> buscarPorIdentificador(String identificador);

    List<ContextoMensagem> buscarContextosExpirados(LocalDateTime dataBase);
}
