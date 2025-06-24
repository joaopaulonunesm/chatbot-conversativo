package com.chatbot.conversativo.adapter.in.rest;

import com.chatbot.conversativo.adapter.in.rest.dto.MensagemContextoRequest;
import com.chatbot.conversativo.application.dto.ContextoMensagemInput;
import com.chatbot.conversativo.application.port.in.GerenciaContextoMensagemUseCase;
import com.chatbot.conversativo.domain.model.ContextoMensagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/contexto-mensagens")
@RequiredArgsConstructor
@Slf4j
public class ContextoMensagemController {

    private final GerenciaContextoMensagemUseCase gerenciaContextoMensagemUseCase;

    @PostMapping
    public ResponseEntity<Void> adicionarMensagem(@RequestBody MensagemContextoRequest request) {
        log.info("Recebendo POST para adicionar mensagem em /v1/contexto-mensagens. Request={}", request);
        String mensagem = Objects.nonNull(request.getMensagem()) ? request.getMensagem().getMensagem() : null;
        ContextoMensagemInput contextoMensagemInput = new ContextoMensagemInput(request.getTelefone(), mensagem);
        gerenciaContextoMensagemUseCase.receberNovaMensagem(contextoMensagemInput);
        log.info("POST para adicionar mensagem em /v1/contexto-mensagens processado com sucesso. Request={}", request);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<ContextoMensagem>> buscarContextoMensagemPorIdentificador(@RequestParam("numeroTelefone") String numeroTelefone) {
        log.info("Recebendo GET para buscar Contexto Mensagem Por Identificador em /v1/contexto-mensagens. numeroTelefone={}", numeroTelefone);
        List<ContextoMensagem> contextoMensagem = gerenciaContextoMensagemUseCase.buscarPorNumeroTelefone(numeroTelefone);
        log.info("GET para buscar Contexto Mensagem Por Identificador em /v1/contexto-mensagens/{numeroTelefone} realizado com sucesso. numeroTelefone={}, contextoMensagem={}", numeroTelefone, contextoMensagem.size());
        return ResponseEntity.ok(contextoMensagem);
    }
}