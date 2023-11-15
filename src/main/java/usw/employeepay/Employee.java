package usw.employeepay;

public class Employee {

    private final int employeeNum;
    private final String name;
    private Salary employeeSalary;

    /**
     * Creates employee
     * @param name Employee name
     * @param employeeNum Employee number
     */
    public Employee(String name, int employeeNum) {
        this.name = name;
        this.employeeNum = employeeNum;
    }

    public String getName() {
        return name;
    }

    public int getEmployeeNum() {
        return employeeNum;
    }

    public Salary getEmployeeSalary() {
        return employeeSalary;
    }

    /**
     * Adds Salary to Employee
     * @param employeeSalary Salary object
     */
    public void setEmployeeSalary(Salary employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

}
