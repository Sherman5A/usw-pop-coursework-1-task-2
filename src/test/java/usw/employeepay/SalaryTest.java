package usw.employeepay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryTest {

    Salary testSalary = new Salary(new BigDecimal("45000"));
    Salary testSalaryDecimal = new Salary(new BigDecimal("50000"));
    Salary testSalaryLarge = new Salary(new BigDecimal("140000"));

    @Test
    @DisplayName("Monthly salary calculations")
    public void monthlySalaryCalculations() {

        BigDecimal expectedMonthlySalary2 = new BigDecimal("3750");
        assertEquals(0, expectedMonthlySalary2.compareTo(testSalary.getMonthlySalary()));

        BigDecimal expectedMonthlySalary1 = new BigDecimal("4166.67");
        assertEquals(0, expectedMonthlySalary1.compareTo(testSalaryDecimal.getMonthlySalary()));

    }

    @Test
    @DisplayName("Taxable amount")
    public void taxableAmount() {
        BigDecimal expectedTaxableAmount = new BigDecimal("32430.00");
        assertEquals(0, expectedTaxableAmount.compareTo(testSalary.getTaxableAmount()));
    }

    @Test
    @DisplayName("Calculate Tax")
    public void calculateTax() {
        BigDecimal expectedTax = new BigDecimal("6486");
        assertEquals(0, expectedTax.compareTo(testSalary.getTotalTax()));

        BigDecimal expectedTaxLarge = new BigDecimal("44175");
        assertEquals(0, expectedTaxLarge.compareTo(testSalaryLarge.getTotalTax()));
    }

    @Test
    @DisplayName("Calculate national insurance")
    void calculateNI() {
        BigDecimal expectedNI = new BigDecimal("4251.84");
        assertEquals(0, expectedNI.compareTo(testSalary.getTotalNI()));
    }

    @Test
    @DisplayName("Parking charge works well")
    void useParkingCharge() {
        BigDecimal expectedNetSalary = new BigDecimal("38394.00");
        BigDecimal monthlyParking = new BigDecimal("10.00");

        assertEquals(0, monthlyParking.compareTo(testSalary.getMonthlyParking()));
        testSalary.applyParkingCharge();
        assertEquals(0, expectedNetSalary.compareTo(testSalary.getNetSalary()));
    }

    @Test
    @DisplayName("Set parking charge works")
    void setParkingCharge() {
        BigDecimal monthlyParking = new BigDecimal("30.00");
        BigDecimal expectedNetSalary = new BigDecimal("38154.00");

        testSalary.setParkingChargeAmount(monthlyParking);
        assertEquals(0, monthlyParking.compareTo(testSalary.getMonthlyParking()));
    }
}