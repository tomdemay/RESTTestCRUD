package com.tdemay.restserver.repository;

import com.tdemay.restserver.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

//@RepositoryRestResource(path="members")
public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {
}
