package com.chatbot.conversativo.application.usecase;

import com.chatbot.conversativo.application.port.out.ContextoMensagemPublisher;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.EventoContextoMensagem;
import com.chatbot.conversativo.domain.model.Mensagem;
import com.chatbot.conversativo.application.port.out.MensagemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class IncluiMensagemNoContextoUseCase {

    private final MensagemRepository mensagemRepository;
    private final ContextoMensagemPublisher contextoMensagemPublisher;

    public void incluir(ContextoMensagem contextoMensagem, Mensagem mensagem) {
        log.info("Incluindo mensagem em contexto. mensagem={}, contextoMensagem={}", mensagem, contextoMensagem);
        contextoMensagem.adicionarMensagem(mensagem);
        mensagemRepository.salvar(contextoMensagem.getIdentificadorGeral(), mensagem);
        contextoMensagemPublisher.notificar(contextoMensagem, EventoContextoMensagem.MENSAGEM_INCLUIDA_NO_CONTEXTO);
        log.info("Mensagem incluida em contexto com sucesso. mensagem={}, contextoMensagem={}", mensagem, contextoMensagem);
    }
}
