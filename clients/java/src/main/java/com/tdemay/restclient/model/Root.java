package com.tdemay.restclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Root {
    @JsonProperty("_embedded")
    private Embedded embedded;
    private Page page;
}
