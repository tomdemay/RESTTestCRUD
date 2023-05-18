import requests
from models import Root, Employee

class EmployeeRestClient:
    def getEmployees() -> list[Employee]:
        print("---------------------------------------------------------------------------")
        print("GET ALL EMPLOYEES")
        result = []
        page = 0 
        while True:
            url = f'http://localhost:8080/api/employees?page={page}&size=3&sort=lastName,firstName,asc'
            page += 1
            response = requests.get(url)
            root = Root(**response.json())
            employees = root._embedded.employees
            for employee in employees:
                print(f"Found: {employee.firstName} {employee.lastName} ({employee.id}): {employee.email}")
            result += employees
            if (len(result) >= root._page.totalElements): break
        print("---------------------------------------------------------------------------")
        return result
    
    def getEmployee(employeeId: int) -> Employee:
        print("---------------------------------------------------------------------------")
        print(f"GET EMPLOYEE {employeeId}")
        url = f'http://localhost:8080/api/employees/{employeeId}'
        response = requests.get(url)
        employee = Employee(**response.json())
        print(f"Found: {employee.firstName} {employee.lastName} ({employee.id}): {employee.email}")
        print("---------------------------------------------------------------------------")
        return employee
    
    def deletedAllEmployees(employees: list[Employee]) -> None:
        print("---------------------------------------------------------------------------")
        print("DELETE ALL EMPLOYEES")
        for employee in employees:
            url = f"http://localhost:8080/api/employees/{employee.id}"
            print(f"Delete: {employee.firstName} {employee.lastName} ({employee.id}): {employee.email}")
            response = requests.delete(url)
        print("---------------------------------------------------------------------------")

    def addEmployees() -> list[Employee]:
        print("---------------------------------------------------------------------------")
        print("ADDING EMPLOYEES")
        employeesToAdd = [
            {"firstName": "Tailer", "lastName": "Dodge", "email": "tailer_dodg@careful-organics.org"},
            {"firstName": "Orsen", "lastName": "Sager", "email": "orsa@diaperstack.com"},
            {"firstName": "Iniga", "lastName": "Hancock", "email": "inighanco@careful-organics.org"},
            {"firstName": "Jamie", "lastName": "Sandor", "email": "jamie.sandor@acusage.net"},
            {"firstName": "Rebekah", "lastName": "Hancock", "email": "rebeka-hancoc@consolidated-farm-research.net"}
        ]
        result = []
        for employee in employeesToAdd:
            url = f'http://localhost:8080/api/employees'
            response = requests.post(url, json=employee)
            employee = Employee(**response.json())
            print(f"Added: {employee.firstName} {employee.lastName} ({employee.id}): {employee.email}")
            result.append(employee)

        print("---------------------------------------------------------------------------")
        return result
    
    def modifyEmployee(employee: Employee) -> Employee:
        print("---------------------------------------------------------------------------")
        print("MODIFYING AN EMPLOYEE")
        url = f'http://localhost:8080/api/employees/{employee.id}'
        response = requests.put(url, json=employee.__dict__)
        result = Employee(**response.json())
        print(f"Modifed: {employee.firstName} {employee.lastName} ({employee.id}): {employee.email}")
        print("---------------------------------------------------------------------------")
        return result
    