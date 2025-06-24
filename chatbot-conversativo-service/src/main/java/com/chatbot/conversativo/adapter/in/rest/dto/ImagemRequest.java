package com.chatbot.conversativo.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ImagemRequest {

    @JsonProperty("imageUrl")
    private String urlImagem;
}
