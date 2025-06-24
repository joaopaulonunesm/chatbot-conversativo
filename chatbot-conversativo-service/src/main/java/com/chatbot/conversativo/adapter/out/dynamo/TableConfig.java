package com.chatbot.conversativo.adapter.out.dynamo;

import com.chatbot.conversativo.adapter.out.dynamo.entity.ContextoMensagemEntity;
import com.chatbot.conversativo.adapter.out.dynamo.entity.MensagemEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class TableConfig {

    public static final String TABELA_CONTEXTO_MENSAGEM = "tb_contexto_mensagem";

    @Bean
    public DynamoDbTable<ContextoMensagemEntity> contextoMensagemTable(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return dynamoDbEnhancedClient.table(TABELA_CONTEXTO_MENSAGEM, TableSchema.fromBean(ContextoMensagemEntity.class));
    }

    @Bean
    public DynamoDbTable<MensagemEntity> mensagemTable(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return dynamoDbEnhancedClient.table(TABELA_CONTEXTO_MENSAGEM, TableSchema.fromBean(MensagemEntity.class));
    }
}
