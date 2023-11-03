package usw.employeepay;

public class Employee {

    private final int employeeNum;
    private final String name;
    private Salary employeeSalary;

    public Employee(String name, int employeeNum, Salary employeeSalary) {
        this.name = name;
        this.employeeNum = employeeNum;
        this.employeeSalary = employeeSalary;
    }
}
