package com.chatbot.conversativo.adapter.in.sqs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SqsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("MessageId")
    private String messageId;

    @JsonProperty("TopicArn")
    private String topicArn;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("UnsubscribeURL")
    private String unsubscribeURL;

    @JsonProperty("MessageAttributes")
    private Map<String, AtributoMessage> messageAttributes;

    @JsonProperty("SignatureVersion")
    private String signatureVersion;

    @JsonProperty("Signature")
    private String signature;

    @JsonProperty("SigningCertURL")
    private String signingCertURL;

}
