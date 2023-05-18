package com.tdemay.restclient.model;

import lombok.Data;

@Data
public class Page {
    private Integer size;
    private Integer totalElements;
    private Integer totalPages;
    private Integer number;
}
