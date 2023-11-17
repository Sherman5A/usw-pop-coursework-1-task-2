package usw.employeepay;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class RateIO implements iRateIO {
    /* LinkedHashMap used as order of bands are important */
    private final LinkedHashMap<BigDecimal, BigDecimal> taxBands = new LinkedHashMap<>();
    private final LinkedHashMap<BigDecimal, BigDecimal> pensionBands = new LinkedHashMap<>();
    private final LinkedHashMap<BigDecimal, BigDecimal> nationalInsurance = new LinkedHashMap<>();
    private BigDecimal monthlyParking;

    /**
     * Reads a CSV for tax bands, national insurance, and
     *
     * @param filePath String of file path
     * @throws IOException If file does not exist / is not found
     */
    public RateIO(String filePath) throws IOException {
        /* Read all lines of the file into a list */
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        /* Iterate through list executing parseLine method on each iteration */
        lines.forEach(line -> parseLine(Arrays.asList(line.split(","))));
    }

    /**
     * Handle the separated line and add it to a tax band
     *
     * @param line Line to parse into rate information
     */
    private void parseLine(List<String> line) {
        /* Each type of deduction possible in CSV */
        switch (line.get(0)) {
            // Add information to respective bands
            case "tax" -> taxBands.put(new BigDecimal(line.get(1)), new BigDecimal(line.get(2)));
            case "pension" -> pensionBands.put(new BigDecimal(line.get(1)), new BigDecimal(line.get(2)));
            case "nationalInsurance" -> nationalInsurance.put(new BigDecimal(line.get(1)), new BigDecimal(line.get(2)));
            case "parking" -> monthlyParking = (new BigDecimal(line.get(1)));
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
