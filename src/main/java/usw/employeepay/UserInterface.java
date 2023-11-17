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

    public static void displayEmployeeInfo(Employee employee) {

        System.out.println("\nCalculating yearly net pay...\n");
        System.out.printf("""
                Gross salary: £%s
                Taxable amount: £%s
                Tax paid: £%s
                National insurance paid: £%s
                """, employee.getSalary().getGrossSalary(), employee.getSalary().getTaxableAmount(),
                employee.getSalary().getIncomeTaxAmount(), employee.getSalary().getNIAmount());

        /* Non-required deductions */
        if (!(employee.getSalary().getTotalParking() == null)) {
            System.out.printf("Parking charge: £%s\n", employee.getSalary().getTotalParking());
        }

        if (!(employee.getSalary().getPensionAmount() == null)) {
            System.out.printf("Pension charge: £%s\n", employee.getSalary().getPensionAmount());
        }

        System.out.println();

        System.out.println("Calculating monthly net pay...\n");
        System.out.printf("""
                Gross salary: £%s
                Taxable amount: £%s
                Tax paid: £%s
                National insurance paid: £%s
                """, Salary.convertMonthly(employee.getSalary().getGrossSalary()),
                Salary.convertMonthly(employee.getSalary().getTaxableAmount()),
                Salary.convertMonthly(employee.getSalary().getIncomeTaxAmount()),
                Salary.convertMonthly(employee.getSalary().getNIAmount()));
        if (!(employee.getSalary().getTotalParking() == null)) {
            System.out.printf("Parking charge: £%s\n", Salary.convertMonthly(employee.getSalary().getTotalParking()));
        }

        if (!(employee.getSalary().getPensionAmount() == null)) {
            System.out.printf("Pension charge: £%s\n", Salary.convertMonthly(employee.getSalary().getPensionAmount()));
        }

        System.out.println();

        System.out.printf("Total deductions: £%s\n", employee.getSalary().getTotalDeductions());
        System.out.println(employee.getSalary().getNetSalary());
        System.out.printf("Monthly net pay: £%s\n", employee.getSalary().getMonthlyNetSalary());
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
    public Salary getSalaryLoop(RateIO rateIO) {

        BigDecimal yearSalary;

        while (true) {
            System.out.print("Yearly salary: ");
            try {
                String inputSalary = scanner.next();
                yearSalary = new BigDecimal(inputSalary);
                yearSalary = yearSalary.setScale(2, RoundingMode.HALF_UP);
                // Clear the newline character from scanner buffer
                // Otherwise next question would appear twice, as the scanner
                // would pick up the leftover newline
                scanner.nextLine();
                System.out.println(yearSalary);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Letter are not allowed employee number");
            }
        }
        return new Salary(yearSalary, rateIO);
    }

    public boolean userApplyParking() {

        while (true) {
            System.out.println("Do you want to apply a parking charge? (y/n)");
            // Normalise to lowercase
            String parkingInput = scanner.nextLine().toLowerCase();
            switch (parkingInput) {
                case "y": {
                    return true;
                }
                case "n": {
                    return false;
                }
            }
        }
    }

    public boolean userApplyPension() {
        while (true) {
            System.out.println("Do you want to apply a teachers pension? (y/n)");
            String parkingInput = scanner.nextLine().toLowerCase();
            switch (parkingInput) {
                case "y": {
                    return true;
                }
                case "n": {
                    return false;
                }
            }
        }
    }


}
