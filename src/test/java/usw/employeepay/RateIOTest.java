package usw.employeepay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test that RateIO reads the CSV file information correctly
 */
class RateIOTest {
    private RateIO rateIO;

    @BeforeEach
    void setUp() {
        try {
            rateIO = new RateIO("rates.csv");

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("CSV tax bands")
    void getTaxBands() {
        LinkedHashMap<BigDecimal, BigDecimal> expectedTaxBands = new LinkedHashMap<>();
        expectedTaxBands.put(new BigDecimal("12570"), new BigDecimal("0.00"));
        expectedTaxBands.put(new BigDecimal("50270"), new BigDecimal("0.20"));
        expectedTaxBands.put(new BigDecimal("125140"), new BigDecimal("0.40"));
        expectedTaxBands.put(new BigDecimal("-1"), new BigDecimal("0.45"));
        assertEquals(expectedTaxBands, rateIO.getTaxBands());
    }

    @Test
    @DisplayName("NI tax bands")
    void getNationalInsurance() {
        LinkedHashMap<BigDecimal, BigDecimal> expectedNationalInsurance = new LinkedHashMap<>();
        expectedNationalInsurance.put(new BigDecimal("9568"), new BigDecimal("0.00"));
        expectedNationalInsurance.put(new BigDecimal("-1"), new BigDecimal("0.12"));
        assertEquals(expectedNationalInsurance, rateIO.getNationalInsurance());
    }

    @Test
    @DisplayName("Pension tax bands")
    void getPensionBands() {
        LinkedHashMap<BigDecimal, BigDecimal> expectedPensionBands = new LinkedHashMap<>();
        expectedPensionBands.put(new BigDecimal("32135.99"), new BigDecimal("0.074"));
        expectedPensionBands.put(new BigDecimal("43259.99"), new BigDecimal("0.086"));
        expectedPensionBands.put(new BigDecimal("51292.99"), new BigDecimal("0.096"));
        expectedPensionBands.put(new BigDecimal("67980.99"), new BigDecimal("0.102"));
        expectedPensionBands.put(new BigDecimal("92597.99"), new BigDecimal("0.113"));
        expectedPensionBands.put(new BigDecimal("-1"), new BigDecimal("0.117"));
        assertEquals(expectedPensionBands, rateIO.getPensionBands());
    }

    @Test
    @DisplayName("CSV parking fee")
    void getMonthlyParking() {
        BigDecimal expectedMonthlyParking = new BigDecimal("10.00");
        assertEquals(0, expectedMonthlyParking.compareTo(rateIO.getMonthlyParking()));
    }
}