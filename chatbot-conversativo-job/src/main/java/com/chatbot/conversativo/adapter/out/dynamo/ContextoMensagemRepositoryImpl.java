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
    public Optional<ContextoMensagem> buscarPorIdentificador(String identificador) {
        String pk = ContextoMensagemEntity.PREFIXO_CHAVE_PARTICAO + identificador;

        QueryConditional query = QueryConditional.sortBeginsWith(
                Key.builder()
                        .partitionValue(pk)
                        .sortValue(ContextoMensagemEntity.PREFIXO_CHAVE_FILTRO)
                        .build()
        );

        SdkIterable<Page<ContextoMensagemEntity>> results = tabelaContextoMensagem.query(query);

        ContextoMensagemEntity contextoMensagemEntity = StreamSupport.stream(results.spliterator(), false)
                .flatMap(page -> page.items().stream())
                .findFirst()
                .orElse(null);

        if (contextoMensagemEntity == null) {
            log.info("Contexto mensagem não encontrado no dynamo. identificador={}", identificador);
            return Optional.empty();
        }

        List<Mensagem> mensagens = mensagemRepository.buscarMensagensPorIdentificador(identificador);

        ContextoMensagem contexto = new ContextoMensagem(
                contextoMensagemEntity.getIdentificador().replace(ContextoMensagemEntity.PREFIXO_CHAVE_PARTICAO, ""),
                contextoMensagemEntity.getSortKey().replace(ContextoMensagemEntity.PREFIXO_CHAVE_FILTRO, ""),
                contextoMensagemEntity.getNumeroTelefone(),
                contextoMensagemEntity.getCriadoEm(),
                contextoMensagemEntity.getExpiraEm(),
                mensagens,
                contextoMensagemEntity.getCodigoSituacao()
        );

        log.info("Retornando contexto mensagem consutlado no dynamo. identificador={}, contextoMensagem={}", identificador, contexto);
        return Optional.of(contexto);
    }

    @Override
    public List<ContextoMensagem> buscarContextosExpirados(LocalDateTime dataBase) {
        SdkIterable<Page<ContextoMensagemEntity>> results = tabelaContextoMensagem
                .index(GSI_CONTEXTO_MENSAGEM_CODIGO_SITUACAO)
                .query(r -> r
                        .queryConditional(QueryConditional.sortLessThanOrEqualTo(
                                Key.builder()
                                        .partitionValue(SituacaoContextoMensagem.ABERTO.getCodigo())
                                        .sortValue(dataBase.truncatedTo(ChronoUnit.SECONDS).toString())
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

        log.info("Contextos expirados encontrados no dynamo: {}", contextos.size());
        return contextos;
    }
}
