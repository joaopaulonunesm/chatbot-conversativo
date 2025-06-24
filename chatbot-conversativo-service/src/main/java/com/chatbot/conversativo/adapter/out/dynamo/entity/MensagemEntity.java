package com.chatbot.conversativo.adapter.out.dynamo.entity;

import com.chatbot.conversativo.configs.LocalDateTimeAttributeConverterConfig;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

@DynamoDbBean
@Setter
@NoArgsConstructor
@ToString
public class MensagemEntity {

    public static final String PREFIXO_CHAVE_PARTICAO = "IDENTIFICADOR#";
    public static final String PREFIXO_CHAVE_FILTRO = "MENSAGEM#";

    private String identificador;
    private String sortKey;
    private String texto;
    private LocalDateTime criadoEm;

    public MensagemEntity(String identificador, String texto, LocalDateTime criadoEm) {
        this.identificador = PREFIXO_CHAVE_PARTICAO.concat(identificador);
        this.sortKey = PREFIXO_CHAVE_FILTRO.concat(UUID.randomUUID().toString());
        this.texto = texto;
        this.criadoEm = criadoEm;
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

    @DynamoDbAttribute("texto_mensagem")
    public String getTexto() {
        return texto;
    }

    @DynamoDbAttribute("criado_em")
    @DynamoDbConvertedBy(LocalDateTimeAttributeConverterConfig.class)
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
