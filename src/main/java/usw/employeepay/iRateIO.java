package usw.employeepay;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Interface for RateIO. Multiple implementations that use file reading,
 * and set values
 */
public interface iRateIO {
    LinkedHashMap<BigDecimal, BigDecimal> getTaxBands();

    LinkedHashMap<BigDecimal, BigDecimal> getNationalInsurance();

    LinkedHashMap<BigDecimal, BigDecimal> getPensionBands();

    BigDecimal getMonthlyParking();
}
