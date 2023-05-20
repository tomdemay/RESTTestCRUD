package com.tdemay.restclient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Embedded {
    private List<Employee> employees = new ArrayList<>();
}
