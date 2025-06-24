package com.chatbot.conversativo.application.usecase;

import com.chatbot.conversativo.application.port.out.ContextoMensagemPublisher;
import com.chatbot.conversativo.application.port.out.ContextoMensagemRepository;
import com.chatbot.conversativo.application.port.out.MensagemRepository;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.EventoContextoMensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CriaContextoMensagemUseCase {

    private final ContextoMensagemRepository contextoMensagemRepository;
    private final ContextoMensagemPublisher contextoMensagemPublisher;

    public ContextoMensagem criar(String numeroTelefone) {
        log.info("Criando contexto mensagem. numeroTelefone={}", numeroTelefone);
        ContextoMensagem contextoMensagem = new ContextoMensagem(numeroTelefone);
        contextoMensagemRepository.salvar(contextoMensagem);
        contextoMensagemPublisher.notificar(contextoMensagem, EventoContextoMensagem.CONTEXTO_MENSAGEM_CRIADO);
        log.info("Contexto mensagem criado com sucesso. numeroTelefone={}", numeroTelefone);
        return contextoMensagem;
    }
}
