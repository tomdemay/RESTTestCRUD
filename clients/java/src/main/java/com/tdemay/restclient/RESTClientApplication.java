package com.tdemay.restclient;

import com.tdemay.restclient.model.Employee;
import com.tdemay.restclient.model.Root;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@SpringBootApplication
public class RESTClientApplication {

	public static List<Employee> getAllEemployees() {
		List<Employee> result = new ArrayList<>();
		System.out.println("---------------------------------------------------------");
		System.out.println("TEST... Get All Employees");

		int page = 0;
		while (true)
		{
			String url = String.format("http://localhost:8080/api/employees?page=%d&size=3&sort=lastName,firstName,asc", page++);
			WebClient.Builder builder = WebClient.builder();
			Root root = builder.build()
					.get()
					.uri(url)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(Root.class)
					.block();

			List<Employee> employeeList = root.getEmbedded().getEmployees();

			for(Employee employee: employeeList) {
				System.out.println(String.format("%s %s (%d): %s", employee.getFirstName(), employee.getLastName(), employee.getId(), employee.getEmail()));
			}
			result.addAll(employeeList);
			if (result.size() >= root.getPage().getTotalElements())
				break;
		}

		System.out.println("---------------------------------------------------------");
		return result;
	}

	public static Employee getEmployee(int employeeId) {
		String url = "http://localhost:8080/api/employees/" + employeeId;
		WebClient.Builder builder = WebClient.builder();
		Employee employee = builder.build()
				.get()
				.uri(url)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Employee.class)
				.block();
		System.out.println("---------------------------------------------------------");
		System.out.println("TEST... Get Employee Id "+ employeeId);

		System.out.println(String.format("%s %s (%d): %s", employee.getFirstName(), employee.getLastName(), employee.getId(), employee.getEmail()));
		System.out.println("---------------------------------------------------------");
		return employee;
	}
	public static void deleteAllEmployees(List<Employee> employees) {
		System.out.println("---------------------------------------------------------");
		System.out.println("TEST... Delete All Employees");

		for (Employee employee: employees) {
			String url = "http://localhost:8080/api/employees/" + employee.getId();
			System.out.println(String.format("DELETING: %s %s (%d): %s", employee.getFirstName(), employee.getLastName(), employee.getId(), employee.getEmail()));
			WebClient.Builder builder = WebClient.builder();
			builder.build()
					.delete()
					.uri(url)
					.retrieve()
					.bodyToMono(Void.class)
					.block();
		}
		System.out.println("---------------------------------------------------------");
	}
	public static List<Employee> addEmployees() {
		System.out.println("---------------------------------------------------------");
		System.out.println("TEST... Add All Employees");

		List<Employee> employees = Arrays.asList(
				new Employee("Tailer","Dodge","tailer_dodg@careful-organics.org"),
				new Employee("Orsen","Sager","orsa@diaperstack.com"),
				new Employee("Iniga","Hancock","inighanco@careful-organics.org"),
				new Employee("Jamie","Sandor","jamie.sandor@acusage.net"),
				new Employee("Rebekah","Hancock","rebeka-hancoc@consolidated-farm-research.net")
		);

		List<Employee> newEmployees = new ArrayList<>();
		for (Employee employee: employees) {
			String url = "http://localhost:8080/api/employees";
			System.out.println(String.format("ADDING: %s %s: %s", employee.getFirstName(), employee.getLastName(), employee.getEmail()));
			WebClient.Builder builder = WebClient.builder();
			Employee newEmployee = builder.build()
					.post()
					.uri(url)
					.body(Mono.just(employee), Employee.class)
					.retrieve()
					.bodyToMono(Employee.class)
					.block();
			System.out.println(String.format("ADDED: %s %s (%d): %s", newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getId(), newEmployee.getEmail()));
			newEmployees.add(newEmployee);
		}
		System.out.println("---------------------------------------------------------");
		return newEmployees;
	}

	public static Employee modifyEmployee(int employeeId, Employee employee) {
		System.out.println("---------------------------------------------------------");
		System.out.println("TEST... Modifying Employees");

		String url = "http://localhost:8080/api/employees/" + employeeId;
		WebClient.Builder builder = WebClient.builder();
		Employee newEmployee = builder.build()
				.put()
				.uri(url)
				.body(Mono.just(employee), Employee.class)
				.retrieve()
				.bodyToMono(Employee.class)
				.block();
		System.out.println(String.format("CHANGED TO: %s %s (%d): %s", newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getId(), newEmployee.getEmail()));
		return newEmployee;
	}

	public static void main(String[] args) {

//		SpringApplication.run(EmployeesApplication.class, args);
//
		List<Employee> employees = getAllEemployees();
		deleteAllEmployees(employees);
		employees = addEmployees();

		Employee employeeToChange = new Employee("Harald", "Hitch", "hara.hi@autozone-inc.info") ;
		Employee employeeChanged = modifyEmployee(employees.get(0).getId(), employeeToChange);

		getEmployee(employeeChanged.getId());
	}
}
