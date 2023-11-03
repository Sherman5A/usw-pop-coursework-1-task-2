package usw.employeepay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

public class Salary {

    // BigDecimal used as we are working with money
    // Avoids errors concerning floating-point representation
    private BigDecimal grossSalary;
    private BigDecimal totalTax;
    private BigDecimal totalNI;
    private BigDecimal totalDeductions;
    private BigDecimal teachersPension;
    private BigDecimal netSalary;
    private boolean parkingCharge = false;
    private BigDecimal monthlyParking = new BigDecimal("10.00");

    public Salary(BigDecimal grossSalary) {
        setSalary(grossSalary);
    }

    public void setSalary(BigDecimal yearlySalary) {
        this.grossSalary = yearlySalary;
        netSalary = yearlySalary;
        totalTax = calculateTax();
        netSalary = netSalary.subtract(totalTax);
        totalNI = calculateNI();
        netSalary = netSalary.subtract(totalNI);

    }

    public void setParkingChargeAmount(BigDecimal monthlyParkingCharge) {
        this.monthlyParking = monthlyParkingCharge;
        if (parkingCharge) {
            applyParkingCharge();
        }
    }

    private BigDecimal calculateTax() {
        LinkedHashMap<BigDecimal, BigDecimal> taxRates = new LinkedHashMap<>();
        taxRates.put(new BigDecimal("12570"), new BigDecimal("0.00"));
        taxRates.put(new BigDecimal("50270"), new BigDecimal("0.20"));
        taxRates.put(new BigDecimal("125140"), new BigDecimal("0.40"));
        // TODO: Better implementation of this at later date
        taxRates.put(new BigDecimal("-1"), new BigDecimal("0.45"));

        BigDecimal taxTotal = new BigDecimal("0");
        BigDecimal previousBracket = new BigDecimal("0");
        for (Map.Entry<BigDecimal, BigDecimal> entry : taxRates.entrySet()) {
            if (entry.getKey().compareTo(BigDecimal.ZERO) < 0) {
                taxTotal = taxTotal.add(grossSalary.subtract(previousBracket).multiply(entry.getValue()));
            } else if (grossSalary.compareTo(entry.getKey()) > 0) {
                taxTotal = taxTotal.add((entry.getKey().subtract(previousBracket)).multiply(entry.getValue()));

            } else if ((grossSalary.compareTo(previousBracket) > 0) && (grossSalary.compareTo(entry.getKey()) < 0)) {
                BigDecimal bracketAmount = grossSalary.subtract(previousBracket);
                taxTotal = taxTotal.add(bracketAmount.multiply(entry.getValue()));
                break;
            }
            previousBracket = entry.getKey();
        }
        return taxTotal;
    }

    private BigDecimal calculateNI() {
        // TODO: CSV gets
        BigDecimal niLimit = new BigDecimal("9568");
        BigDecimal niRate = new BigDecimal("0.12");
        // totalNI = (grossSalary - niLimit) * niRate
        return (grossSalary.subtract(niLimit)).multiply(niRate);
    }

    public void applyParkingCharge() {
        parkingCharge = true;
        netSalary = netSalary.subtract(monthlyParking.multiply(new BigDecimal("12")));
    }

    public void applyTeachersPension() {
    }



    public BigDecimal getMonthlyParking() {
        return monthlyParking;
    }

    public BigDecimal getMonthlySalary() {
        return this.grossSalary.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTaxableAmount() {
        return grossSalary.subtract(new BigDecimal("12570"));
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public BigDecimal getTotalNI() {
        return totalNI;
    }

    public BigDecimal getTeachersPension() {
        return teachersPension;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }
}
