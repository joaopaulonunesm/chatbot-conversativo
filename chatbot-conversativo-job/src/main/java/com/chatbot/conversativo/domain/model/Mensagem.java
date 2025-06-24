package com.chatbot.conversativo.domain.model;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Mensagem {

    private final String texto;
    private final LocalDateTime criadaEm;

    public Mensagem(String texto, LocalDateTime criadaEm) {
        this.texto = texto;
        this.criadaEm = criadaEm;
    }
}
