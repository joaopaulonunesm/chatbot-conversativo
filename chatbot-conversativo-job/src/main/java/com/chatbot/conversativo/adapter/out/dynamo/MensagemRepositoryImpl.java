package com.chatbot.conversativo.adapter.out.dynamo;

import com.chatbot.conversativo.adapter.out.dynamo.entity.MensagemEntity;
import com.chatbot.conversativo.application.port.out.MensagemRepository;
import com.chatbot.conversativo.domain.model.Mensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class MensagemRepositoryImpl implements MensagemRepository {

    private final DynamoDbTable<MensagemEntity> tabelaMensagem;

    @Override
    public List<Mensagem> buscarMensagensPorIdentificador(String identificador) {
        String pk = MensagemEntity.PREFIXO_CHAVE_PARTICAO + identificador;

        QueryConditional query = QueryConditional.sortBeginsWith(
                Key.builder()
                        .partitionValue(pk)
                        .sortValue(MensagemEntity.PREFIXO_CHAVE_FILTRO)
                        .build()
        );

        SdkIterable<Page<MensagemEntity>> results = tabelaMensagem.query(query);

        List<Mensagem> mensagens = StreamSupport.stream(results.spliterator(), false)
                .flatMap(page -> page.items().stream())
                .map(item -> new Mensagem(item.getTexto(), item.getCriadoEm()))
                .collect(Collectors.toList());

        log.info("Retornando mensagens relacionadas ao contexto consultadas no dynamo. identificador={}, mensagens={}", identificador, mensagens);
        return mensagens;
    }
}
