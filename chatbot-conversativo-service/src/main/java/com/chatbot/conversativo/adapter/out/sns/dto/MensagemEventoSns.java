package com.chatbot.conversativo.adapter.out.sns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MensagemEventoSns {

    @JsonProperty("texto")
    private String texto;
    @JsonProperty("criadaEm")
    private LocalDateTime criadaEm;
}
