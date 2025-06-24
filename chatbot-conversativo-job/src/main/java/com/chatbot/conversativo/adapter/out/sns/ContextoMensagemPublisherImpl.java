package com.chatbot.conversativo.adapter.out.sns;

import com.chatbot.conversativo.adapter.out.sns.dto.ContextoMensagemEventoSns;
import com.chatbot.conversativo.adapter.out.sns.dto.MensagemEventoSns;
import com.chatbot.conversativo.application.port.out.ContextoMensagemPublisher;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.EventoContextoMensagem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContextoMensagemPublisherImpl implements ContextoMensagemPublisher {

    public static final String HEADER_EVENTO_MENSAGEM = "evento";

    @Value("${chatbot-conversativo.topico-sns-arn}")
    private String topicoContextoMensagemArn;

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Override
    public void notificar(ContextoMensagem contextoMensagem, EventoContextoMensagem eventoContextoMensagem) {
        try {
            log.info("Notificando contexto mensagem no SNS. topicoContextoMensagemArn={}", topicoContextoMensagemArn);
            ContextoMensagemEventoSns contextoMensagemEventoSns = mapToEventoSns(contextoMensagem);
            String eventoSns = objectMapper.writeValueAsString(contextoMensagemEventoSns);

            Map<String, MessageAttributeValue> headers = getHeaders(eventoContextoMensagem);

            PublishRequest request = PublishRequest.builder()
                    .message(eventoSns)
                    .messageAttributes(headers)
                    .topicArn(topicoContextoMensagemArn)
                    .build();

            log.info("Notificando evento contexto mensagem via SNS. topicoContextoMensagemArn={}, headers={}, body={}", topicoContextoMensagemArn, headers, eventoSns);

            snsClient.publish(request);

            log.info("Evento contexto mensagem notificado via SNS com sucesso. topicoContextoMensagemArn={}, headers={}, body={}", topicoContextoMensagemArn, headers, eventoSns);
        } catch (Exception exception) {
            log.error("Erro ao notificar contexto mensagem via SNS. contextoMensagem={}, eventoContextoMensagem={}", contextoMensagem, eventoContextoMensagem);
            throw new RuntimeException("Erro ao notificar contexto mensagem via SNS. contextoMensagem=" + contextoMensagem + ", eventoContextoMensagem=" + eventoContextoMensagem, exception);
        }
    }

    private ContextoMensagemEventoSns mapToEventoSns(ContextoMensagem contextoMensagem) {
        return ContextoMensagemEventoSns.builder()
                .identificador(contextoMensagem.getIdentificadorGeral())
                .mensagens(contextoMensagem.getMensagens().stream()
                        .map(mensagem -> MensagemEventoSns.builder()
                                .texto(mensagem.getTexto())
                                .criadaEm(mensagem.getCriadaEm())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Map<String, MessageAttributeValue> getHeaders(EventoContextoMensagem eventoContextoMensagem) {
        Map<String, MessageAttributeValue> headers = new HashMap<>();

        MessageAttributeValue valorHeaderEventoMensagem = MessageAttributeValue.builder()
                .dataType("String")
                .stringValue(eventoContextoMensagem.getCodigoEvento())
                .build();

        headers.put(HEADER_EVENTO_MENSAGEM, valorHeaderEventoMensagem);
        return headers;
    }
}
