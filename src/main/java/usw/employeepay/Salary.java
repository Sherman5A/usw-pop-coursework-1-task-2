package usw.employeepay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

public class Salary {

    // BigDecimal used as we are working with money
    // Avoids errors concerning floating-point representation
    private BigDecimal yearlySalary;
    private BigDecimal taxableAmount;
    private BigDecimal taxAmount;
    private BigDecimal netSalary;
    private boolean parkingCharge = false;
    private BigDecimal monthlyParkingCharge = new BigDecimal("10.00");

    public Salary(BigDecimal yearlySalary) {
        setSalary(yearlySalary);
    }

    public void setSalary(BigDecimal yearlySalary) {
        this.yearlySalary = yearlySalary;
        netSalary = yearlySalary;
        taxableAmount = this.yearlySalary.subtract(new BigDecimal("12570"));
        taxAmount = calculateTax();
        netSalary = netSalary.subtract(taxAmount);
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
                taxTotal = taxTotal.add(yearlySalary.subtract(previousBracket).multiply(entry.getValue()));
            } else if (yearlySalary.compareTo(entry.getKey()) > 0) {
                taxTotal = taxTotal.add((entry.getKey().subtract(previousBracket)).multiply(entry.getValue()));

            } else if ((yearlySalary.compareTo(previousBracket) > 0) && (yearlySalary.compareTo(entry.getKey()) < 0)) {
                BigDecimal bracketAmount = yearlySalary.subtract(previousBracket);
                taxTotal = taxTotal.add(bracketAmount.multiply(entry.getValue()));
                break;
            }
            previousBracket = entry.getKey();
        }
        return taxTotal;
    }

    public void useParkingCharge() {
        parkingCharge = true;
        netSalary = netSalary.subtract(monthlyParkingCharge.multiply(new BigDecimal("12")));
    }


    public void setParkingCharge(BigDecimal monthlyParkingCharge) {
        this.monthlyParkingCharge = monthlyParkingCharge;
        if (parkingCharge) {
            useParkingCharge();
        }
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public BigDecimal getMonthlyParkingCharge() {
        return monthlyParkingCharge;
    }

    public BigDecimal getMonthlySalary() {
        return this.yearlySalary.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public BigDecimal getTax() {
        return taxAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
}
