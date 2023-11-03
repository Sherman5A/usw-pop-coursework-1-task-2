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
        totalDeductions = totalTax.add(totalNI);
        netSalary = netSalary.subtract(totalNI);

    }

    public void setParkingChargeAmount(BigDecimal monthlyParkingCharge) {
        this.monthlyParking = monthlyParkingCharge;
        if (parkingCharge) {
            applyParkingCharge();
        }
    }

    private BigDecimal calculateTax() {
        LinkedHashMap<BigDecimal, BigDecimal> taxBands = new LinkedHashMap<>();
        taxBands.put(new BigDecimal("12570"), new BigDecimal("0.00"));
        taxBands.put(new BigDecimal("50270"), new BigDecimal("0.20"));
        taxBands.put(new BigDecimal("125140"), new BigDecimal("0.40"));
        taxBands.put(new BigDecimal("-1"), new BigDecimal("0.45"));
        return applyPaymentBands(grossSalary, taxBands);
    }

    private BigDecimal applyPaymentBands(BigDecimal income, LinkedHashMap<BigDecimal, BigDecimal> paymentBands) {
        BigDecimal totalPayment = new BigDecimal("0");
        BigDecimal previousBracket = new BigDecimal("0");
        for (Map.Entry<BigDecimal, BigDecimal> entry : paymentBands.entrySet()) {
            if (entry.getKey().compareTo(BigDecimal.ZERO) < 0) {
                totalPayment =
                        totalPayment.add(income.subtract(previousBracket).multiply(entry.getValue()).setScale(2,
                                RoundingMode.HALF_UP));
            } else if (income.compareTo(entry.getKey()) > 0) {
                totalPayment =
                        totalPayment.add((entry.getKey().subtract(previousBracket)).multiply(entry.getValue()).setScale(2, RoundingMode.HALF_UP));

            } else if ((income.compareTo(previousBracket) > 0) && (income.compareTo(entry.getKey()) < 0)) {
                BigDecimal bracketAmount = income.subtract(previousBracket);
                totalPayment = totalPayment.add(bracketAmount.multiply(entry.getValue()).setScale(2, RoundingMode.HALF_UP));
                break;
            }
            previousBracket = entry.getKey();
        }
        return totalPayment;
    }

    private BigDecimal calculateNI() {
        // TODO: CSV gets
        BigDecimal niLimit = new BigDecimal("9568");
        BigDecimal niRate = new BigDecimal("0.12");
        // totalNI = (grossSalary - niLimit) * niRate
        return (grossSalary.subtract(niLimit)).multiply(niRate);
    }

    private BigDecimal calculatePension() {
        LinkedHashMap<BigDecimal, BigDecimal> pensionBands = new LinkedHashMap<>();
        pensionBands.put(new BigDecimal("32135.99"), new BigDecimal("0.074"));
        pensionBands.put(new BigDecimal("43259.99"), new BigDecimal("0.086"));
        pensionBands.put(new BigDecimal("51292.99"), new BigDecimal("0.096"));
        pensionBands.put(new BigDecimal("67980.99"), new BigDecimal("0.102"));
        pensionBands.put(new BigDecimal("92597.99"), new BigDecimal("0.113"));
        pensionBands.put(new BigDecimal("-1"), new BigDecimal("0.117"));
        return applyPaymentBands(grossSalary, pensionBands);
    }

    public void applyParkingCharge() {
        parkingCharge = true;
        netSalary = netSalary.subtract(monthlyParking.multiply(new BigDecimal("12")));
    }

    public void applyTeachersPension() {
        teachersPension = calculatePension();
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

    public BigDecimal getTotalTeachersPension() {
        return teachersPension;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

}
