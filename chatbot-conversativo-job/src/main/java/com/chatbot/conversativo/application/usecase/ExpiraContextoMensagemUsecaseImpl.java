package com.chatbot.conversativo.application.usecase;

import com.chatbot.conversativo.application.port.in.ExpiraContextoMensagemUsecase;
import com.chatbot.conversativo.application.port.out.ContextoMensagemPublisher;
import com.chatbot.conversativo.application.port.out.ContextoMensagemRepository;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.EventoContextoMensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class ExpiraContextoMensagemUsecaseImpl implements ExpiraContextoMensagemUsecase {

    private final ContextoMensagemRepository contextoMensagemRepository;
    private final ContextoMensagemPublisher contextoMensagemPublisher;

    @Override
    public void expirar() {
        List<ContextoMensagem> contextos = contextoMensagemRepository.buscarContextosExpirados(LocalDateTime.now());

        if (CollectionUtils.isEmpty(contextos)) {
            log.debug("Nenhum contexto mensagem expirado foi encontrado.");
            return;
        }

        contextos.stream().parallel().forEach(contextoMensagem -> {
            contextoMensagem.expirarContexto();
            contextoMensagemRepository.salvar(contextoMensagem);
            contextoMensagemPublisher.notificar(contextoMensagem, EventoContextoMensagem.CONTEXTO_MENSAGEM_EXPIRADO);
        });
    }
}
