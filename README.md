# chatbot-conversativo

## O projeto
O projeto chatbot-conversativo explora uma solução para recebimento de mensagens de canais como o whatsapp e gerencia o contexto das mensagens que o usuário digitou em uma janela de tempo e por fim, integra com uma IA que faz a interpretação das mensagens, gerando tanto uma lista de operações e via webhook chamando um serviço externo para processamento dessas solicitações solicitadas quanto uma resposta ao cliente no canal solicitado.

#### Por exemplo: 
o usuário inicia uma conversa via whatsapp e a cada mensagem que é enviada a API nos notifica, porém muitas vezes o cliente digita mais de uma mensagem para solicitar uma unica operação ou ainda ele já pode enviar n mensagens nessa janela de tempo soliciando n operações. Após a janela de tempo expirar é onde enviamos uma unica vez o processamento desse contexto para o serviço externo e também a resposta ao usuário com base no conjunto das mensagens do contexto.

## Tecnologias
    Java
    Spring
    Spring scheduling
    Dynamo
    SNS
    SQS
    API Rest

## Como subir o projeto local

### Premissas
- Ter o java configurado em sua máquina
- Ter o docker instalado
- Ter o terraform instalado

### Como fazer
#### Primeiro passo
- fazer o clone desse projeto em sua máquina
- `git clone https://github.com/joaopaulonunesm/chatbot-conversativo.git`
#### Segundo passo
- Subir a imagem do localstack
- acessar a pasta chatbot-conversativo-infra
- executar o comando `docker-compose up -d`
#### Terceiro passo
- Aplicar a infra do projeto no localstack via terraform
- acessar a pasta chatbot-conversativo-infra/terraform
- executar o comando `terraform init`
- executar o comando `terraform apply`
#### Quarto passo
- Subir os dois serviços tanto o do job quanto o do service
- Recomendo subir via IDE ambos os projetos em paralelo para acompanhamento de logs e possibilidade de debug
