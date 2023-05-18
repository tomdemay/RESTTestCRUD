package com.tdemay.restserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="employee", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}, name="uk_email"))
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id ;
    @Column(name = "first_name", nullable = false, length=45)
    @NonNull
    public String firstName ;
    @Column(name = "last_name", nullable = false, length=45)
    @NonNull
    public String lastName ;
    @Column(name = "email", nullable = false, length=45)
    @NonNull
    public String email;
}
