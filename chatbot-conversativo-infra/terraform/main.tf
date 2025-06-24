provider "aws" {
  access_key = "test"
  secret_key = "test"
  region     = "sa-east-1"

  s3_use_path_style           = true
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true

  endpoints {
    sqs            = "http://localhost:4566"
    sns            = "http://localhost:4566"
    dynamodb       = "http://localhost:4566"
  }
}

resource "aws_sqs_queue" "processa_contexto_mensagem_expirado_sqs" {
  name = "processa-contexto-mensagem-expirado-sqs"
}

resource "aws_sqs_queue" "processa_contexto_mensagem_expirado_sqs_dlq" {
  name = "processa-contexto-mensagem-expirado-sqs-dlq"
}

resource "aws_sns_topic" "contexto-mensagem-sns" {
  name = "contexto-mensagem-sns"
}

resource "aws_sns_topic_subscription" "queue_subscription" {
  topic_arn              = aws_sns_topic.contexto-mensagem-sns.arn
  protocol               = "sqs"
  endpoint               = aws_sqs_queue.processa_contexto_mensagem_expirado_sqs.arn
  endpoint_auto_confirms = true

  filter_policy = jsonencode({
    evento = ["CONTEXTO_MENSAGEM_EXPIRADO"]
  })
}

resource "aws_sqs_queue_policy" "sqs_policy" {
  queue_url = aws_sqs_queue.processa_contexto_mensagem_expirado_sqs.id
  policy    = <<POLICY
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": "*",
        "Action": "SQS:SendMessage",
        "Resource": "${aws_sqs_queue.processa_contexto_mensagem_expirado_sqs.arn}",
        "Condition": {
          "ArnEquals": {
            "aws:SourceArn": "${aws_sns_topic.contexto-mensagem-sns.arn}"
          }
        }
      }
    ]
  }
  POLICY
}

resource "aws_sqs_queue_redrive_policy" "redrive_policy" {
  queue_url = aws_sqs_queue.processa_contexto_mensagem_expirado_sqs.id
  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.processa_contexto_mensagem_expirado_sqs_dlq.arn
    maxReceiveCount     = 2
  })
}

resource "aws_dynamodb_table" "tb_contexto_mensagem" {
  name           = "tb_contexto_mensagem"
  read_capacity  = 5
  write_capacity = 5

  attribute {
    name = "codigo_chave_particao"
    type = "S"
  }

  attribute {
    name = "codigo_chave_filtro"
    type = "S"
  }

  hash_key = "codigo_chave_particao"
  range_key = "codigo_chave_filtro"

  attribute {
    name = "numero_telefone"
    type = "S"
  }

  attribute {
    name = "codigo_situacao"
    type = "S"
  }

  attribute {
    name = "expira_em"
    type = "S"
  }

  global_secondary_index {
    name            = "gsi_contexto_mensagem_codigo_situacao"
    hash_key        = "codigo_situacao"
    range_key       = "expira_em"
    projection_type = "ALL"
    read_capacity   = 5
    write_capacity  = 5
  }

    global_secondary_index {
      name            = "gsi_contexto_mensagem_numero_telefone"
      hash_key        = "numero_telefone"
      range_key       = "codigo_situacao"
      projection_type = "ALL"
      read_capacity   = 5
      write_capacity  = 5
    }
}