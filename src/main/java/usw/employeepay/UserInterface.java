package usw.employeepay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
    }

    public void userLoop() {

        String employeeName;
        int employeeNumber;
        BigDecimal yearSalary;

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

        Employee employee = new Employee(employeeName, employeeNumber, new Salary(yearSalary));
    }
}
