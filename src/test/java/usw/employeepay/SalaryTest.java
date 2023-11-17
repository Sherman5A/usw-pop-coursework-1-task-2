package usw.employeepay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryTest {

    /* Use the fake rateIO, changing CSV won't mess up unit tests */
    TestingFakeRateIO testingRateIO = new TestingFakeRateIO();

    /* Various salaries to test different tax bands */
    Salary testSalary = new Salary(new BigDecimal("45000"), testingRateIO);
    Salary testSalaryDecimal = new Salary(new BigDecimal("50000"), testingRateIO);
    Salary testSalaryLarge = new Salary(new BigDecimal("140000"), testingRateIO);

    @Test
    @DisplayName("Calculate monthly salary")
    public void monthlySalaryCalculations() {

        BigDecimal expectedMonthlySalary2 = new BigDecimal("3750");
        assertEquals(0, expectedMonthlySalary2.compareTo(testSalary.getMonthlySalary()));

        BigDecimal expectedMonthlySalary1 = new BigDecimal("4166.67");
        assertEquals(0, expectedMonthlySalary1.compareTo(testSalaryDecimal.getMonthlySalary()));

    }

    @Test
    @DisplayName("Calculate taxable amount")
    public void getTaxableAmount() {
        BigDecimal expectedTaxableAmount = new BigDecimal("32430.00");
        assertEquals(0, expectedTaxableAmount.compareTo(testSalary.getTaxableAmount()));
    }

    @Test
    @DisplayName("Calculate income tax")
    public void calculateIncomeTax() {
        BigDecimal expectedTax = new BigDecimal("6486");
        testSalary.applyIncomeTax();
        assertEquals(0, expectedTax.compareTo(testSalary.getIncomeTaxAmount()));

        BigDecimal expectedTaxLarge = new BigDecimal("44175");
        testSalaryLarge.applyIncomeTax();
        assertEquals(0, expectedTaxLarge.compareTo(testSalaryLarge.getIncomeTaxAmount()));
    }

    @Test
    @DisplayName("Calculate national insurance")
    void calculateNationalInsurance() {
        BigDecimal expectedNI = new BigDecimal("4251.84");
        testSalary.applyNationalInsurance();
        assertEquals(0, expectedNI.compareTo(testSalary.getNIAmount()));
    }

    @Test
    @DisplayName("Parking charge applies")
    void useParkingCharge() {
        BigDecimal expectedNetSalary = new BigDecimal("34142.16");
        BigDecimal monthlyParking = new BigDecimal("120.00");
        testSalary.applyMandatoryDeductions();
        testSalary.applyParkingCharge();
        assertEquals(0, monthlyParking.compareTo(testSalary.getTotalParking()));
        assertEquals(0, expectedNetSalary.compareTo(testSalary.getNetSalary()));
    }

    @Test
    @DisplayName("Total teachers pension")
    void getTotalTeachersPension() {
        BigDecimal expectedTeachersPension = new BigDecimal("3501.76");
        testSalary.applyPension();
        assertEquals(0, expectedTeachersPension.compareTo(testSalary.getPensionAmount()));
    }

    @Test
    @DisplayName("Total deductions")
    void getTotalDeductions() {
        BigDecimal expectedDeductions = new BigDecimal("10737.84");
        testSalary.applyMandatoryDeductions();
        assertEquals(0, expectedDeductions.compareTo(testSalary.getTotalDeductions()));
    }

    @Test
    @DisplayName("Net salary")
    void getNetSalary() {
        BigDecimal expectedNetSalary = new BigDecimal("34142.16");
        testSalary.applyMandatoryDeductions();
        testSalary.applyParkingCharge();
        assertEquals(0, expectedNetSalary.compareTo(testSalary.getNetSalary()));
    }
}