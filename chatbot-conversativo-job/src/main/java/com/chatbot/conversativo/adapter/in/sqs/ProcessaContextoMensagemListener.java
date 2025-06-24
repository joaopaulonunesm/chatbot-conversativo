package com.chatbot.conversativo.adapter.in.sqs;

import com.chatbot.conversativo.adapter.in.sqs.models.ContextoMensagemEventoSqs;
import com.chatbot.conversativo.adapter.in.sqs.models.SqsMessage;
import com.chatbot.conversativo.application.dto.ProcessaContextoMensagemInput;
import com.chatbot.conversativo.application.port.in.ProcessaContextoMensagemUsecase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Component
@Slf4j
public class ProcessaContextoMensagemListener {

    private String filaTarifasSqs;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final ReceiveMessageRequest receiveMessageRequest;
    private final ProcessaContextoMensagemUsecase processaContextoMensagemUsecase;

    public ProcessaContextoMensagemListener(@Value("${chatbot-conversativo.processa-contexto-mensagem-expirado-sqs-url}") String sqsUrl,
                                            SqsClient sqsClient,
                                            ObjectMapper objectMapper,
                                            ProcessaContextoMensagemUsecase processaContextoMensagemUsecase) {
        this.filaTarifasSqs = sqsUrl;
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsUrl)
                .waitTimeSeconds(1)
                .maxNumberOfMessages(10)
                .build();
        this.processaContextoMensagemUsecase = processaContextoMensagemUsecase;
    }

    @Scheduled(fixedDelay = 1000)
    public void listener() {
        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);
        response.messages().forEach(message -> {
            consumirMensagem(message);
            deletarMensagem(message);
        });
    }

    private void consumirMensagem(Message mensagem) {
        try {
            log.info("Consumindo mensagem. mensagem={}", mensagem);

            SqsMessage sqsMessage = objectMapper.readValue(mensagem.body(), SqsMessage.class);
            ContextoMensagemEventoSqs contextoMensagemEventoSqs = objectMapper.readValue(sqsMessage.getMessage(), ContextoMensagemEventoSqs.class);

            ProcessaContextoMensagemInput processaContextoMensagemInput = new ProcessaContextoMensagemInput(contextoMensagemEventoSqs.getIdentificador());
            processaContextoMensagemUsecase.processarContexto(processaContextoMensagemInput);

            log.info("Mensagem consumida com sucesso! mensagem={}", mensagem);
        } catch (Exception exception) {
            log.error("Erro ao consumir mensagem do SQS.", exception);
            throw new RuntimeException("Erro ao consumir mensagem do SQS", exception);
        }
    }

    private void deletarMensagem(Message message) {
        sqsClient.deleteMessage(builder ->
                builder.queueUrl(filaTarifasSqs)
                        .receiptHandle(message.receiptHandle()));
    }
}
