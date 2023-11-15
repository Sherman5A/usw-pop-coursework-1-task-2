package usw.employeepay;

import java.math.BigDecimal;
import java.util.LinkedHashMap;


/**
 * Class created to emulate RateIO with no input reading. This ensures that
 * the testing for SalaryTest is not dynamic and does not depend on a file
 */
public class TestingFakeRateIO implements iRateIO {

    private final LinkedHashMap<BigDecimal, BigDecimal> taxBands = new LinkedHashMap<>();
    private final LinkedHashMap<BigDecimal, BigDecimal> nationalInsurance = new LinkedHashMap<>();
    private final LinkedHashMap<BigDecimal, BigDecimal> pensionBands = new LinkedHashMap<>();
    private BigDecimal monthlyParking;


    public TestingFakeRateIO() {
        taxBands.put(new BigDecimal("12570"), new BigDecimal("0.00"));
        taxBands.put(new BigDecimal("50270"), new BigDecimal("0.20"));
        taxBands.put(new BigDecimal("125140"), new BigDecimal("0.40"));
        taxBands.put(new BigDecimal("-1"), new BigDecimal("0.45"));

        nationalInsurance.put(new BigDecimal("9568"), new BigDecimal("0.00"));
        nationalInsurance.put(new BigDecimal("-1"), new BigDecimal("0.12"));

        pensionBands.put(new BigDecimal("32135.99"), new BigDecimal("0.074"));
        pensionBands.put(new BigDecimal("43259.99"), new BigDecimal("0.086"));
        pensionBands.put(new BigDecimal("51292.99"), new BigDecimal("0.096"));
        pensionBands.put(new BigDecimal("67980.99"), new BigDecimal("0.102"));
        pensionBands.put(new BigDecimal("92597.99"), new BigDecimal("0.113"));
        pensionBands.put(new BigDecimal("-1"), new BigDecimal("0.117"));

    }

    public LinkedHashMap<BigDecimal, BigDecimal> getTaxBands() {
        return taxBands;
    }

    public LinkedHashMap<BigDecimal, BigDecimal> getNationalInsurance() {
        return nationalInsurance;
    }

    public LinkedHashMap<BigDecimal, BigDecimal> getPensionBands() {
        return pensionBands;
    }

    public BigDecimal getMonthlyParking() {
        return monthlyParking;
    }
}
