package com.chatbot.conversativo.application.port.in;

import com.chatbot.conversativo.application.dto.ProcessaContextoMensagemInput;

public interface ProcessaContextoMensagemUsecase {

    void processarContexto(ProcessaContextoMensagemInput processaContextoMensagemInput);
}
