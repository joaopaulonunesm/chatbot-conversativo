package com.chatbot.conversativo.configs;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LocalDateTimeAttributeConverterConfig implements AttributeConverter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public AttributeValue transformFrom(LocalDateTime localDateTime) {
        return AttributeValue.builder()
                .s(localDateTime.truncatedTo(ChronoUnit.SECONDS).format(FORMATTER))
                .build();
    }

    @Override
    public LocalDateTime transformTo(AttributeValue attributeValue) {
        return LocalDateTime.parse(attributeValue.s(), FORMATTER);
    }

    @Override
    public EnhancedType<LocalDateTime> type() {
        return EnhancedType.of(LocalDateTime.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
