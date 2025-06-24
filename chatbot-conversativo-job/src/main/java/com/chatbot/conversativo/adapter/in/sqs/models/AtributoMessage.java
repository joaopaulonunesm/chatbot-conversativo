package com.chatbot.conversativo.adapter.in.sqs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtributoMessage {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Value")
    private String value;
}
