package usw.employeepay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

public class Salary {

    private final BigDecimal monthlyParking = new BigDecimal("10.00");
    iRateIO rateIO;
    /**
     * BigDecimal used as we are working with money
     * Avoids errors concerning floating-point representation
     */
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private BigDecimal totalDeductions = new BigDecimal("0");
    private BigDecimal totalIncomeTax;
    private BigDecimal totalNI;
    private BigDecimal totalPension;
    private BigDecimal totalParking;
    private final boolean parkingCharge = false;


    public Salary(BigDecimal grossSalary, iRateIO rateIO) {
        this.grossSalary = grossSalary;
        netSalary = grossSalary;
        this.rateIO = rateIO;
    }


    /**
     * Applies required deductions, income tax and national insurance
     */
    public void applyMandatoryDeductions() {
        applyIncomeTax();
        applyNationalInsurance();
    }

    public void applyIncomeTax() {
        totalIncomeTax = applyPaymentBands(grossSalary, rateIO.getTaxBands());
        totalDeductions = totalDeductions.add(totalIncomeTax);
        netSalary = netSalary.subtract(totalIncomeTax);
    }

    public void applyNationalInsurance() {
        totalNI = applyPaymentBands(grossSalary, rateIO.getNationalInsurance());
        totalDeductions = totalDeductions.add(totalNI);
        netSalary = netSalary.subtract(totalNI);
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
                totalPayment = totalPayment.add(bracketAmount.multiply(entry.getValue()).setScale(2,
                        RoundingMode.HALF_UP));
                break;
            }
            previousBracket = entry.getKey();
        }
        return totalPayment;
    }

    private BigDecimal calculatePension() {
        return applyPaymentBands(grossSalary, rateIO.getPensionBands());
    }

    public void applyParkingCharge() {
        System.out.println(rateIO.getMonthlyParking());
        totalParking = rateIO.getMonthlyParking().multiply(new BigDecimal("12"));
        totalDeductions = totalDeductions.add(totalParking);
        netSalary = netSalary.subtract(totalParking);
    }

    public void applyTeachersPension() {
        totalPension = calculatePension();
        totalDeductions = totalDeductions.subtract(totalPension);
        netSalary = netSalary.subtract(totalPension);
    }

    // Setters

    public void setSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
        netSalary = grossSalary;
        applyMandatoryDeductions();
    }

    public void setRateIO(iRateIO rateIO) {
        this.rateIO = rateIO;
        applyMandatoryDeductions();
    }

    // Getters

    public BigDecimal getMonthlySalary() {
        return grossSalary.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTaxableAmount() {
        return grossSalary.subtract(new BigDecimal("12570"));
    }

    public BigDecimal getIncomeTaxAmount() {
        return totalIncomeTax;
    }

    public BigDecimal getNIAmount() {
        return totalNI;
    }

    public BigDecimal getPensionAmount() {
        return totalPension;
    }

    public BigDecimal getParkingAmount() {
        return monthlyParking;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public BigDecimal getMonthlyNetSalary() {
        return netSalary.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

}
