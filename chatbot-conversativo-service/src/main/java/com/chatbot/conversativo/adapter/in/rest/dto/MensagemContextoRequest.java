package com.chatbot.conversativo.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MensagemContextoRequest {

    @JsonProperty("phone")
    private String telefone;

    @JsonProperty("text")
    private MensagemRequest mensagem;

    @JsonProperty("image")
    private ImagemRequest imagem;
}
