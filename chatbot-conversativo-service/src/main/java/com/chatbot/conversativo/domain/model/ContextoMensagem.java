package com.chatbot.conversativo.domain.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class ContextoMensagem {

    public static final int SEGUNDOS_PARA_EXPIRACAO_CONTEXTO = 20;

    private String identificadorGeral;
    private String identificadorContextoMensagem;
    private String numeroTelefone;
    private List<Mensagem> mensagens;
    private LocalDateTime criadoEm;
    private LocalDateTime expiraEm;
    private SituacaoContextoMensagem situacao;

    public ContextoMensagem(String numeroTelefone) {
        if (!StringUtils.hasText(numeroTelefone)) {
            throw new IllegalArgumentException("Numero telefone não informado.");
        }

        this.identificadorGeral = UUID.randomUUID().toString();
        this.identificadorContextoMensagem = UUID.randomUUID().toString();
        this.numeroTelefone = numeroTelefone;
        this.mensagens = new ArrayList<>();
        this.criadoEm = LocalDateTime.now();
        definirDataExpiracao();
        this.situacao = SituacaoContextoMensagem.ABERTO;
    }

    public ContextoMensagem(String identificadorGeral, String identificadorContextoMensagem, String numeroTelefone, LocalDateTime criadoEm, LocalDateTime expiraEm, List<Mensagem> mensagens, String codigoSituacaoContextoMensagem) {
        this.identificadorGeral = identificadorGeral;
        this.identificadorContextoMensagem = identificadorContextoMensagem;
        this.numeroTelefone = numeroTelefone;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
        this.mensagens = mensagens;
        this.situacao = SituacaoContextoMensagem.valueByCodigo(codigoSituacaoContextoMensagem);
    }

    public void adicionarMensagem(Mensagem mensagem) {
        if (mensagem == null) {
            throw new IllegalArgumentException("Mensagem não pode ser nula.");
        }
        this.mensagens.add(mensagem);
    }

    public List<Mensagem> getMensagens() {
        return Collections.unmodifiableList(mensagens);
    }

    public void definirDataExpiracao() {
        this.expiraEm = LocalDateTime.now().plusSeconds(SEGUNDOS_PARA_EXPIRACAO_CONTEXTO);
    }
}
