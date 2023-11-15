package usw.employeepay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;


    /**
     * Class that handles outputting and accepting user input
     *
     * @param scanner Input handling
     */
    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
    }


    /**
     * UI loop constructs an Employee class and returns it
     * Uses validation
     *
     * @return Constructed Employee object
     */
    public Employee createEmployeeLoop() {

        String employeeName;
        int employeeNumber;

        System.out.println("Welcome to USW Employee Salary Calculator");
        System.out.println("-----------------------------------------");

        while (true) {
            System.out.print("Employee Name: ");
            employeeName = scanner.nextLine();
            if (!employeeName.isEmpty()) {
                break;
            }
            System.out.println("Empty inputs are not accepted.");
        }

        while (true) {
            System.out.print("Employee number: ");
            try {
                employeeNumber = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Letter are not allowed employee number");
                scanner.nextLine();
            }
        }

        return new Employee(employeeName, employeeNumber);
    }


    /**
     * UI loop that constructs Salary that is filled with tax information
     *
     * @param rateIO The tax bands to use in initial instantiation of taxes, pension, etc.
     * @return Constructed Salary object
     */
    public Salary createSalaryLoop(RateIO rateIO) {

        BigDecimal yearSalary;

        while (true) {
            System.out.print("Yearly salary: ");
            try {
                String inputSalary = scanner.next();
                yearSalary = new BigDecimal(inputSalary);
                yearSalary = yearSalary.setScale(2, RoundingMode.HALF_UP);
                System.out.println(yearSalary);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Letter are not allowed employee number");
            }
        }
        return new Salary(yearSalary, rateIO);
    }
}
