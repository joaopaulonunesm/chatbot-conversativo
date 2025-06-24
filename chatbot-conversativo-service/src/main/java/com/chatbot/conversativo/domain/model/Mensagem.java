package com.chatbot.conversativo.domain.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@ToString
public class Mensagem {

    private final String texto;
    private final LocalDateTime criadaEm;

    public Mensagem(String texto) {
        this.texto = texto;
        this.criadaEm = LocalDateTime.now();
        validarMensagem();
    }

    public Mensagem(String texto, LocalDateTime criadaEm) {
        this.texto = texto;
        this.criadaEm = criadaEm;
        validarMensagem();
    }

    private void validarMensagem() {
        if (!StringUtils.hasText(this.texto)) {
            throw new IllegalArgumentException("Texto da mensagem não informado.");
        }
    }
}
