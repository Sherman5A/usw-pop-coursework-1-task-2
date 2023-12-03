package usw.employeepay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that contains information and methods related to Salary.
 * Includes: income tax, national insurance, pensions, and parking charges
 */
public class Salary {

    iRateIO rateIO;

    /*
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

    public Salary(BigDecimal grossSalary, iRateIO rateIO) {
        this.grossSalary = grossSalary;
        netSalary = grossSalary;
        this.rateIO = rateIO;
    }

    public static BigDecimal convertMonthly(BigDecimal amount) {
        return amount.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
    }

    /**
     * Applies required deductions: income tax, national insurance
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

    public void applyPension() {
        totalPension = applyPaymentBands(grossSalary, rateIO.getPensionBands());
        totalDeductions = totalDeductions.add(totalPension);
        netSalary = netSalary.subtract(totalPension);
    }

    public void applyParkingCharge() {
        // Monthly parking * 12
        totalParking = rateIO.getMonthlyParking().multiply(new BigDecimal("12"));
        totalDeductions = totalDeductions.add(totalParking);
        netSalary = netSalary.subtract(totalParking);
    }

    /**
     * Applies payment bands to income dynamically
     *
     * @param income       Accepts BigDecimals, no negatives
     * @param paymentBands LinkedHashMap containing, the taxBand first, then the taxRate, overflow tax rates should
     *                     be denoted wih a negative number on the band
     * @return Total payment on income after paymentBands applied
     */
    private BigDecimal applyPaymentBands(BigDecimal income, LinkedHashMap<BigDecimal, BigDecimal> paymentBands) {
        BigDecimal totalPayment = new BigDecimal("0");
        BigDecimal previousBracket = new BigDecimal("0");
        for (Map.Entry<BigDecimal, BigDecimal> entry : paymentBands.entrySet()) {
            BigDecimal currentBracket = entry.getKey();
            BigDecimal bracketRate = entry.getValue();

            /*
             * If the payment is in a band denoted with a negative number then it is overflow, and applies
             * that rate to rest of salary
             */
            if (currentBracket.compareTo(BigDecimal.ZERO) < 0) {
                // totalPayment = totalPayment + (income - previousBand) * taxRate
                totalPayment =
                        totalPayment.add(income.subtract(previousBracket).multiply(bracketRate).setScale(2,
                                RoundingMode.HALF_UP));
            } else if (income.compareTo(currentBracket) > 0) {
                /* If the income is greater than the current payment band */

                /* totalPayment = totalPayment + (currentBracket - previousBand) * taxRate
                 * It then rounds to 2 decimal places
                 */
                totalPayment =
                        totalPayment.add((currentBracket.subtract(previousBracket)).multiply(bracketRate).setScale(2, RoundingMode.HALF_UP));

            } else if ((income.compareTo(previousBracket) > 0) && (income.compareTo(currentBracket) < 0)) {
                /* If the income is smaller than the current payment band */

                /* Get the leftover money in the band */
                BigDecimal bracketAmount = income.subtract(previousBracket);
                /* apply tax to the leftover amount in the band
                 * totalPayment = totalPayment + (leftoverAmount * taxRate)
                 */
                totalPayment = totalPayment.add(bracketAmount.multiply(bracketRate).setScale(2,
                        RoundingMode.HALF_UP));
                /* Since income is smaller than current band, won't make it to next band, break out of loop */
                break;
            }
            previousBracket = currentBracket;
        }
        return totalPayment;
    }

    public void setSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
        netSalary = grossSalary;
        applyMandatoryDeductions();
    }

    public void setRateIO(iRateIO rateIO) {
        this.rateIO = rateIO;
        applyMandatoryDeductions();
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public BigDecimal getMonthlySalary() {
        return convertMonthly(grossSalary);
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

    public BigDecimal getTotalParking() {
        return totalParking;
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
