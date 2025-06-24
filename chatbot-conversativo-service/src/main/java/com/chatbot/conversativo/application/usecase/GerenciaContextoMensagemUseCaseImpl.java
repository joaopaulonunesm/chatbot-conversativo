package com.chatbot.conversativo.application.usecase;

import com.chatbot.conversativo.application.dto.ContextoMensagemInput;
import com.chatbot.conversativo.application.port.in.GerenciaContextoMensagemUseCase;
import com.chatbot.conversativo.application.port.out.ContextoMensagemRepository;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import com.chatbot.conversativo.domain.model.Mensagem;
import com.chatbot.conversativo.domain.model.SituacaoContextoMensagem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class GerenciaContextoMensagemUseCaseImpl implements GerenciaContextoMensagemUseCase {

    private final ContextoMensagemRepository contextoMensagemRepository;
    private final CriaContextoMensagemUseCase criaContextoMensagemUseCase;
    private final IncluiMensagemNoContextoUseCase incluiMensagemNoContextoUseCase;

    @Override
    public void receberNovaMensagem(ContextoMensagemInput mensagemDTO) {
        String numeroTelefone = mensagemDTO.getNumeroTelefone();
        Mensagem mensagem = new Mensagem(mensagemDTO.getTexto());

        contextoMensagemRepository.buscarPorNumeroTelefoneEStatus(numeroTelefone, SituacaoContextoMensagem.ABERTO)
                .stream()
                .filter(contexto -> contexto.getExpiraEm().isAfter(LocalDateTime.now()))
                .findFirst()
                .ifPresentOrElse(contextoMensagem -> {
                    incluiMensagemNoContextoUseCase.incluir(contextoMensagem, mensagem);
                    contextoMensagem.definirDataExpiracao();
                    contextoMensagemRepository.salvar(contextoMensagem);
                }, () -> {
                    ContextoMensagem contextoMensagem = criaContextoMensagemUseCase.criar(numeroTelefone);
                    incluiMensagemNoContextoUseCase.incluir(contextoMensagem, mensagem);
                });
    }

    @Override
    public List<ContextoMensagem> buscarPorNumeroTelefone(String numeroTelefone) {
        return contextoMensagemRepository.buscarPorNumeroTelefone(numeroTelefone).stream()
                .sorted(Comparator.comparing(ContextoMensagem::getCriadoEm))
                .collect(Collectors.toList());
    }
}
