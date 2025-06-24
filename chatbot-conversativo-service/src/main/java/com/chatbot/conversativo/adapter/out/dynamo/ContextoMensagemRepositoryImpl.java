package com.chatbot.conversativo.adapter.out.dynamo;

import com.chatbot.conversativo.adapter.out.dynamo.entity.ContextoMensagemEntity;
import com.chatbot.conversativo.application.port.out.ContextoMensagemRepository;
import com.chatbot.conversativo.application.port.out.MensagemRepository;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.Mensagem;
import com.chatbot.conversativo.domain.model.SituacaoContextoMensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.chatbot.conversativo.adapter.out.dynamo.entity.ContextoMensagemEntity.GSI_CONTEXTO_MENSAGEM_CODIGO_SITUACAO;
import static com.chatbot.conversativo.adapter.out.dynamo.entity.ContextoMensagemEntity.GSI_CONTEXTO_MENSAGEM_NUMERO_TELEFONE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContextoMensagemRepositoryImpl implements ContextoMensagemRepository {

    private final MensagemRepository mensagemRepository;
    private final DynamoDbTable<ContextoMensagemEntity> tabelaContextoMensagem;

    @Override
    public void salvar(ContextoMensagem contextoMensagem) {
        ContextoMensagemEntity entity = new ContextoMensagemEntity(
                contextoMensagem.getIdentificadorGeral(),
                contextoMensagem.getIdentificadorContextoMensagem(),
                contextoMensagem.getNumeroTelefone(),
                contextoMensagem.getCriadoEm(),
                contextoMensagem.getExpiraEm(),
                contextoMensagem.getSituacao().getCodigo());

        tabelaContextoMensagem.putItem(entity);
        log.info("Contexto mensagem salvo no dynamo com sucesso. entity={}", entity);
    }

    @Override
    public List<ContextoMensagem> buscarPorNumeroTelefone(String numeroTelefone) {
        SdkIterable<Page<ContextoMensagemEntity>> results = tabelaContextoMensagem
                .index(GSI_CONTEXTO_MENSAGEM_NUMERO_TELEFONE)
                .query(r -> r
                        .queryConditional(QueryConditional.keyEqualTo(
                                Key.builder()
                                        .partitionValue(numeroTelefone)
                                        .build()
                        ))
                );

        List<ContextoMensagem> contextos = StreamSupport.stream(results.spliterator(), false)
                .flatMap(page -> page.items().stream())
                .map(contextoMensagemEntity -> {
                    String identificador = contextoMensagemEntity.getIdentificador().replace(ContextoMensagemEntity.PREFIXO_CHAVE_PARTICAO, "");
                    List<Mensagem> mensagens = mensagemRepository.buscarMensagensPorIdentificador(identificador);
                    return new ContextoMensagem(
                            identificador,
                            contextoMensagemEntity.getSortKey().replace(ContextoMensagemEntity.PREFIXO_CHAVE_FILTRO, ""),
                            contextoMensagemEntity.getNumeroTelefone(),
                            contextoMensagemEntity.getCriadoEm(),
                            contextoMensagemEntity.getExpiraEm(),
                            mensagens,
                            contextoMensagemEntity.getCodigoSituacao()
                    );
                })
                .toList();

        log.info("Contextos por numero telefone e status encontrados no dynamo: {}", contextos.size());
        return contextos;
    }

    @Override
    public List<ContextoMensagem> buscarPorNumeroTelefoneEStatus(String numeroTelefone, SituacaoContextoMensagem situacaoContextoMensagem) {
        SdkIterable<Page<ContextoMensagemEntity>> results = tabelaContextoMensagem
                .index(GSI_CONTEXTO_MENSAGEM_NUMERO_TELEFONE)
                .query(r -> r
                        .queryConditional(QueryConditional.sortLessThanOrEqualTo(
                                Key.builder()
                                        .partitionValue(numeroTelefone)
                                        .sortValue(situacaoContextoMensagem.getCodigo())
                                        .build()
                        ))
                );

        List<ContextoMensagem> contextos = StreamSupport.stream(results.spliterator(), false)
                .flatMap(page -> page.items().stream())
                .map(contextoMensagemEntity -> {
                    String identificador = contextoMensagemEntity.getIdentificador().replace(ContextoMensagemEntity.PREFIXO_CHAVE_PARTICAO, "");
                    List<Mensagem> mensagens = mensagemRepository.buscarMensagensPorIdentificador(identificador);
                    return new ContextoMensagem(
                            identificador,
                            contextoMensagemEntity.getSortKey().replace(ContextoMensagemEntity.PREFIXO_CHAVE_FILTRO, ""),
                            contextoMensagemEntity.getNumeroTelefone(),
                            contextoMensagemEntity.getCriadoEm(),
                            contextoMensagemEntity.getExpiraEm(),
                            mensagens,
                            contextoMensagemEntity.getCodigoSituacao()
                    );
                })
                .toList();

        log.info("Contextos por numero telefone e status encontrados no dynamo: {}", contextos.size());
        return contextos;
    }
}
