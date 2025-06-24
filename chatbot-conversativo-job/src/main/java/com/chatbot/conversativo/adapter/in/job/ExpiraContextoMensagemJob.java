package com.chatbot.conversativo.adapter.in.job;

import com.chatbot.conversativo.application.port.in.ExpiraContextoMensagemUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiraContextoMensagemJob {

    private final ExpiraContextoMensagemUsecase expiraContextoMensagemUsecase;

    @Scheduled(fixedDelay = 20_000)
    public void processar() {
        log.debug("Iniciando JOB de processamento de contexto de mensagem");
        expiraContextoMensagemUsecase.expirar();
        log.debug("JOB de processamento de contexto de mensagem finalizado");
    }
}
