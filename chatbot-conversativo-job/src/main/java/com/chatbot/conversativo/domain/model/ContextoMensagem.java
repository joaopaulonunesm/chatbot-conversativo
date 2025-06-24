package com.chatbot.conversativo.domain.model;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class ContextoMensagem {

    private String identificadorGeral;
    private String identificadorContextoMensagem;
    private String numeroTelefone;
    private List<Mensagem> mensagens;
    private LocalDateTime criadoEm;
    private LocalDateTime expiraEm;
    private SituacaoContextoMensagem situacao;

    public ContextoMensagem(String identificadorGeral, String identificadorContextoMensagem, String numeroTelefone, LocalDateTime criadoEm, LocalDateTime expiraEm, List<Mensagem> mensagens, String codigoSituacaoContextoMensagem) {
        this.identificadorGeral = identificadorGeral;
        this.identificadorContextoMensagem = identificadorContextoMensagem;
        this.numeroTelefone = numeroTelefone;
        this.criadoEm = criadoEm;
        this.expiraEm = expiraEm;
        this.mensagens = mensagens;
        this.situacao = SituacaoContextoMensagem.valueByCodigo(codigoSituacaoContextoMensagem);
    }

    public List<Mensagem> getMensagens() {
        return Collections.unmodifiableList(mensagens);
    }

    public void expirarContexto() {
        if (this.expiraEm.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("O contexto não deve ser expirado pois ainda não atingiu a data de expiração.");
        }

        this.situacao = SituacaoContextoMensagem.EXPIRADO;
    }

    public void processarContexto() {
        if (!this.situacao.equals(SituacaoContextoMensagem.EXPIRADO)) {
            throw new RuntimeException("O status atual do contexto não permite processamento.");
        }

        this.situacao = SituacaoContextoMensagem.PROCESSADO;
    }
}
