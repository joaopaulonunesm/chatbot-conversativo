package com.chatbot.conversativo.adapter.in.sqs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MensagemEventoSqs {

    @JsonProperty("texto")
    private String texto;
    @JsonProperty("criadaEm")
    private LocalDateTime criadaEm;
}
