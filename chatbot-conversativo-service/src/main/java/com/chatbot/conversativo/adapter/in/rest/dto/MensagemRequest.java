package com.chatbot.conversativo.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MensagemRequest {

    @JsonProperty("message")
    private String mensagem;
}
