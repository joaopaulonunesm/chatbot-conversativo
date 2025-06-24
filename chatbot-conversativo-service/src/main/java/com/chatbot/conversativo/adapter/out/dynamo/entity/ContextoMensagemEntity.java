package com.chatbot.conversativo.adapter.out.dynamo.entity;

import com.chatbot.conversativo.configs.LocalDateTimeAttributeConverterConfig;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;

@DynamoDbBean
@Setter
@NoArgsConstructor
@ToString
public class ContextoMensagemEntity {

    public static final String PREFIXO_CHAVE_PARTICAO = "IDENTIFICADOR#";
    public static final String PREFIXO_CHAVE_FILTRO = "CONTEXTO-MENSAGEM#";
    public static final String GSI_CONTEXTO_MENSAGEM_CODIGO_SITUACAO = "gsi_contexto_mensagem_codigo_situacao";
    public static final String GSI_CONTEXTO_MENSAGEM_NUMERO_TELEFONE = "gsi_contexto_mensagem_numero_telefone";

    private String identificador;
    private String sortKey;
    private String numeroTelefone;
    private LocalDateTime criadoEm;
    private LocalDateTime expiraEm;
    private String codigoSituacao;

    public ContextoMensagemEntity(String identificador, String sortKey, String numeroTelefone, LocalDateTime criadoEm, LocalDateTime expiraEm, String codigoSituacao) {
        this.identificador = PREFIXO_CHAVE_PARTICAO.concat(identificador);
        this.sortKey = PREFIXO_CHAVE_FILTRO.concat(sortKey);
        this.numeroTelefone = numeroTelefone;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
        this.codigoSituacao = codigoSituacao;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("codigo_chave_particao")
    public String getIdentificador() {
        return identificador;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("codigo_chave_filtro")
    public String getSortKey() {
        return sortKey;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = GSI_CONTEXTO_MENSAGEM_NUMERO_TELEFONE)
    @DynamoDbAttribute("numero_telefone")
    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    @DynamoDbAttribute("criado_em")
    @DynamoDbConvertedBy(LocalDateTimeAttributeConverterConfig.class)
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    @DynamoDbSecondarySortKey(indexNames = GSI_CONTEXTO_MENSAGEM_CODIGO_SITUACAO)
    @DynamoDbAttribute("expira_em")
    @DynamoDbConvertedBy(LocalDateTimeAttributeConverterConfig.class)
    public LocalDateTime getExpiraEm() {
        return expiraEm;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = GSI_CONTEXTO_MENSAGEM_CODIGO_SITUACAO)
    @DynamoDbSecondarySortKey(indexNames = GSI_CONTEXTO_MENSAGEM_NUMERO_TELEFONE)
    @DynamoDbAttribute("codigo_situacao")
    public String getCodigoSituacao() {
        return codigoSituacao;
    }
}
