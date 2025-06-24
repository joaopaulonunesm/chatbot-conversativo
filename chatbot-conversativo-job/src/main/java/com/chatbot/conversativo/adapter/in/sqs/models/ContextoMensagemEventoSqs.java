package com.chatbot.conversativo.adapter.in.sqs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ContextoMensagemEventoSqs {

    @JsonProperty("identificador")
    private String identificador;
}
