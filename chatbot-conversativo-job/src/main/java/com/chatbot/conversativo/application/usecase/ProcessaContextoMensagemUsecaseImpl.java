package com.chatbot.conversativo.application.usecase;

import com.chatbot.conversativo.application.dto.ProcessaContextoMensagemInput;
import com.chatbot.conversativo.application.port.in.ProcessaContextoMensagemUsecase;
import com.chatbot.conversativo.application.port.out.ContextoMensagemPublisher;
import com.chatbot.conversativo.application.port.out.ContextoMensagemRepository;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.EventoContextoMensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProcessaContextoMensagemUsecaseImpl implements ProcessaContextoMensagemUsecase {

    private final ContextoMensagemRepository contextoMensagemRepository;
    private final ContextoMensagemPublisher contextoMensagemPublisher;

    @Override
    public void processarContexto(ProcessaContextoMensagemInput processaContextoMensagemInput) {
        ContextoMensagem contextoMensagem = contextoMensagemRepository.buscarPorIdentificador(processaContextoMensagemInput.getIdentificador())
                .orElseThrow(() -> new RuntimeException("Não foi encontrado contexto para processamento com o identificador informado. identificador=" + processaContextoMensagemInput.getIdentificador()));
        contextoMensagem.processarContexto();
        contextoMensagemRepository.salvar(contextoMensagem);

        // TODO implementar interação com IA para interpretar as mensagens do contexto. Pedir a ele um json de retorno com uma lista de operações solicitadas e uma resposta ao usuário final com base em todas as mensagens do contexto.

        // TODO implementar uma chamada WEBHOOK contendo a lista de operações solicitadas pelo usuário

        // TODO implementar a chamada para a api do canal com a resposta da IA

        contextoMensagemPublisher.notificar(contextoMensagem, EventoContextoMensagem.CONTEXTO_MENSAGEM_PROCESSADO);
    }
}
