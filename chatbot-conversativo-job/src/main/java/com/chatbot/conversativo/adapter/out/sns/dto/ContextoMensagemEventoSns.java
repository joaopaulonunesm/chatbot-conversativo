package com.chatbot.conversativo.adapter.out.sns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ContextoMensagemEventoSns {

    @JsonProperty("identificador")
    private String identificador;

    @JsonProperty("mensagens")
    private List<MensagemEventoSns> mensagens;
}
