from models import Employee
from employees import EmployeeRestClient


if __name__ == "__main__":
    employees = EmployeeRestClient.getEmployees()
    EmployeeRestClient.deletedAllEmployees(employees)
    employees = EmployeeRestClient.addEmployees()

    employeeId = employees[0].id
    employee = EmployeeRestClient.getEmployee(employeeId)

    employee = Employee(id=employeeId, firstName="Harald", lastName="Hitch", email="hara.hi-autozone.inc.info")
    EmployeeRestClient.modifyEmployee(employee)
