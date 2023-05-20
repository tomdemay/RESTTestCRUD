package com.tdemay.restclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.tdemay.restclient.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdemay.restclient.model.Root;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RESTClientApplication
{
    private static String convertStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    // An example of how to make a RESTful call using jackson and HttpClient. The rest of the examples
    // use Spring WebFlux WebClient
    public static <T> T getPOJOFromRestServerUsingHttpClient(String url, Class<T> cls) throws IOException {
        T pojo = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create an instance of HttpGet request
            HttpGet request = new HttpGet(url);

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the response entity
                HttpEntity entity = response.getEntity();
                // Convert the entity content to a string
                String jsonString = convertStreamToString(entity.getContent());
                // Create an instance of ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                // ignore properties we don't care about
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // Convert JSON to POJO
                pojo = mapper.readValue(jsonString, cls);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return pojo;
    }

    public static List<Employee> getAllEmployees() throws IOException {
        List<Employee> result = new ArrayList<>();
        System.out.println("---------------------------------------------------------");
        System.out.println("TEST... Get All Employees using Jackson and HTTPClient");

        int page = 0;
        while (true) {
            String url = String.format("http://localhost:8080/api/employees?page=%d&size=3&sort=lastName,firstName,asc", page++);
            Root root = getPOJOFromRestServerUsingHttpClient(url, Root.class);
            List<Employee> employeeList = root.getEmbedded().getEmployees();
            result.addAll(employeeList);
            if (result.size() >= root.getPage().getTotalElements())
                break;
        }
        for(Employee employee: result) {
            System.out.println(String.format("%s %s (%d): %s", employee.getFirstName(), employee.getLastName(), employee.getId(), employee.getEmail()));
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
        System
        .out.println("---------------------------------------------------------");
        System.out.println("TEST... Get Employee Id "+ employeeId +" using Spring WebFlux WebClient");

        System.out.println(String.format("%s %s (%d): %s", employee.getFirstName(), employee.getLastName(), employee.getId(), employee.getEmail()));
        System.out.println("---------------------------------------------------------");
        return employee;
    }

    public static void deleteAllEmployees(List<Employee> employees) {
		System.out.println("---------------------------------------------------------");
		System.out.println("TEST... Delete All Employees using Spring WebFlux WebClient");

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
        System.out.println("TEST... Add All Employees using Spring WebFlux WebClient");

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

    public static void main(String[] args) throws IOException
    {
        List<Employee> employees = getAllEmployees();
        deleteAllEmployees(employees);
        employees = addEmployees();

        Employee employeeToChange = new Employee("Harald", "Hitch", "hara.hi@autozone-inc.info") ;
        Employee employeeChanged = modifyEmployee(employees.get(0).getId(), employeeToChange);

        getEmployee(employeeChanged.getId());
    }
}
