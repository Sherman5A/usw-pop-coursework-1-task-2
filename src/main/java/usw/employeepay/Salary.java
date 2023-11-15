package usw.employeepay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

public class Salary {

    // BigDecimal used as we are working with money
    // Avoids errors concerning floating-point representation
    private BigDecimal grossSalary;
    private BigDecimal incomeTax;
    private BigDecimal totalNI;
    private BigDecimal totalDeductions = new BigDecimal("0");
    private BigDecimal teachersPension;
    private BigDecimal netSalary;
    private boolean parkingCharge = false;
    private BigDecimal monthlyParking = new BigDecimal("10.00");
    iRateIO rateIO;


    public Salary(BigDecimal grossSalary, iRateIO rateIO) {
        this.grossSalary = grossSalary;
        netSalary = grossSalary;
        this.rateIO = rateIO;
    }

    public void setSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
        netSalary = grossSalary;
        applyAllDeductions();
    }

    public void setRateIO(iRateIO rateIO) {
        this.rateIO = rateIO;
        applyAllDeductions();
    }

    public void applyAllDeductions() {
        applyIncomeTax();
        applyNationalInsurance();
    }

    public void applyIncomeTax() {
        incomeTax = applyPaymentBands(grossSalary, rateIO.getTaxBands());
        totalDeductions = totalDeductions.add(incomeTax);
        netSalary = netSalary.subtract(incomeTax);
    }

    public void applyNationalInsurance() {
        totalNI = applyPaymentBands(grossSalary, rateIO.getNationalInsurance());
        totalDeductions = totalDeductions.add(totalNI);
        netSalary = netSalary.subtract(totalNI);
    }

    public void setParkingChargeAmount(BigDecimal monthlyParkingCharge) {
        this.monthlyParking = monthlyParkingCharge;
        if (parkingCharge) {
            applyParkingCharge();
        }
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

    private BigDecimal calculateNI(LinkedHashMap<BigDecimal, BigDecimal> niBands) {
        // TODO: CSV gets
        return applyPaymentBands(grossSalary, niBands);
    }

    private BigDecimal calculatePension() {
        return applyPaymentBands(grossSalary, rateIO.getPensionBands());
    }

    public void applyParkingCharge() {
        parkingCharge = true;
        netSalary = netSalary.subtract(monthlyParking.multiply(new BigDecimal("12")));
    }

    public void applyTeachersPension() {
        teachersPension = calculatePension();
    }

    public BigDecimal getMonthlySalary() {
        return this.grossSalary.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTaxableAmount() {
        return grossSalary.subtract(new BigDecimal("12570"));
    }

    public BigDecimal getIncomeTax() {
        return incomeTax;
    }

    public BigDecimal getTotalNI() {
        return totalNI;
    }

    public BigDecimal getTotalTeachersPension() {
        return teachersPension;
    }
    public BigDecimal getMonthlyParking() {
        return monthlyParking;
    }
    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

}
