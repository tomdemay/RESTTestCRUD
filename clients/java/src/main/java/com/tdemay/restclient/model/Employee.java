package com.tdemay.restclient.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;
}
