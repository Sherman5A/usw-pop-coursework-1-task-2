package usw.employeepay;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class RateIO {
    private final LinkedHashMap<BigDecimal, BigDecimal> taxBands = new LinkedHashMap<>();
    private final LinkedHashMap<BigDecimal, BigDecimal> pensionBands = new LinkedHashMap<>();
    private final LinkedHashMap<BigDecimal, BigDecimal> nationalInsurance = new LinkedHashMap<>();
    private BigDecimal monthlyParking;

    public RateIO(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lines.forEach(line -> parseLine(Arrays.asList(line.split(","))));
    }

    private void parseLine(List<String> line) {
        switch (line.get(0)) {
            case "tax" -> taxBands.put(new BigDecimal(line.get(1)), new BigDecimal(line.get(2)));
            case "pension" -> pensionBands.put(new BigDecimal(line.get(1)), new BigDecimal(line.get(2)));
            case "nationalInsurance" -> nationalInsurance.put(new BigDecimal(line.get(1)), new BigDecimal(line.get(2)));
            case "parking" -> {
                System.out.println("Parking");
                monthlyParking = (new BigDecimal(line.get(1)));
            }
        }
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
