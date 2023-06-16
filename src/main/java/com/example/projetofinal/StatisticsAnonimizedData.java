package com.example.projetofinal;

import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.aggregates.StatisticsBuilder;
import org.deidentifier.arx.aggregates.quality.QualityMeasureColumnOriented;
import org.deidentifier.arx.risk.RiskEstimateBuilder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class StatisticsAnonimizedData {

    private final DataHandle handlerData;
    private final StatisticsBuilder statisticsBuilder;
    private int kValue;
    private double epsilon;
    private double delta;

    public StatisticsAnonimizedData(DataHandle data, int kValue) {
        this.handlerData = data;
        this.statisticsBuilder = handlerData.getStatistics();
        this.kValue = kValue;
    }

    public StatisticsAnonimizedData(DataHandle data, double epsilon, double delta) {
        this.handlerData = data;
        this.statisticsBuilder = handlerData.getStatistics();
        this.epsilon = epsilon;
        this.delta = delta;
    }

    public String emptyLineStats() {
        String result = ";";
        for (int index = 0; index < handlerData.getDefinition().getQuasiIdentifyingAttributes().size(); index++) {
            result = result.concat(";;;; ;");
        }
        result = result.concat(";;;");
        return result;
    }

    public String emptyLineRisk() {
        return ";".repeat(3);
    }

    // Function that returns the number of rows in the dataset without considering the suppressed data
    public String getSupressedData() {
        int dadosSuprimidos = 0;
        for (int indexRow = 0; indexRow < handlerData.getNumRows(); indexRow++) {
            if (handlerData.isSuppressed(indexRow)) {
                dadosSuprimidos++;
            }
        }
        return dadosSuprimidos + ";";
    }

    // Function that receives the array of quasi-identifiers and returns a String with the column oriented measures
    public String getQualityAttributes() {
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.000", decimalFormatSymbols);

        QualityMeasureColumnOriented generalizationIntensity = statisticsBuilder.getQualityStatistics().getGeneralizationIntensity();
        QualityMeasureColumnOriented missings = statisticsBuilder.getQualityStatistics().getMissings();
        QualityMeasureColumnOriented entropy = statisticsBuilder.getQualityStatistics().getNonUniformEntropy();
        QualityMeasureColumnOriented squaredError = statisticsBuilder.getQualityStatistics().getAttributeLevelSquaredError();

        String result = "";

        for (String attribute : handlerData.getDefinition().getQuasiIdentifyingAttributes()) {
            result = result.concat(decimalFormat.format(generalizationIntensity.getValue(attribute) * 100) + ";" + decimalFormat.format(missings.getValue(attribute) * 100) + ";" + decimalFormat.format(entropy.getValue(attribute) * 100) + ";" + decimalFormat.format(squaredError.getValue(attribute) * 100) + "; ;");
        }

        return result;
    }

    // Function that returns a String with the row oriented measures
    public String getQualityRecords() {
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.000", decimalFormatSymbols);

        double discernibility = statisticsBuilder.getQualityStatistics().getDiscernibility().getValue() * 100;
        double averageClassSize = statisticsBuilder.getQualityStatistics().getAverageClassSize().getValue() * 100;
        double squaredError = statisticsBuilder.getQualityStatistics().getRecordLevelSquaredError().getValue() * 100;

        return decimalFormat.format(discernibility) + ";" + decimalFormat.format(averageClassSize) + ";" + decimalFormat.format(squaredError) + ";";
    }

    // Function that returns a String with the k value
    public String getKValue() {
        return kValue + ";";
    }

    public String getEpsilonValue() {
        return epsilon + ";";
    }

    public String getDeltaValue() {
        return delta + ";";
    }

    // Function that returns a String with the risk measures
    public String getRiskMeasures() {
        if (handlerData == null) return getKValue() + emptyLineRisk();

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.000", decimalFormatSymbols);

        RiskEstimateBuilder estimate = handlerData.getRiskEstimator();
        double prosecutorRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk() * 100;
        double journalistRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedJournalistRisk() * 100;
        double marketerRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedMarketerRisk() * 100;

        // Return  the K value and the risk measures (they should be in % with 2 decimal places)
        return getKValue() + decimalFormat.format(prosecutorRisk) + ";" + decimalFormat.format(journalistRisk) + ";" + decimalFormat.format(marketerRisk) + ";";
    }

    // Function that returns a String with all the statistics
    public String getFullStatistics() {
        if (handlerData == null) return getKValue() + emptyLineStats();

        return getKValue() + getSupressedData() + getQualityAttributes() + getQualityRecords();
    }

    public String getRiskMeasuresDiferencial() {
        if (handlerData == null) return getEpsilonValue() + getDeltaValue() + emptyLineRisk();

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.000", decimalFormatSymbols);

        RiskEstimateBuilder estimate = handlerData.getRiskEstimator();
        double prosecutorRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk() * 100;
        double journalistRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedJournalistRisk() * 100;
        double marketerRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedMarketerRisk() * 100;

        // Return  the K value and the risk measures (they should be in % with 2 decimal places)
        return getEpsilonValue() + getDeltaValue() + decimalFormat.format(prosecutorRisk) + ";" + decimalFormat.format(journalistRisk) + ";" + decimalFormat.format(marketerRisk) + ";";
    }

    public String getFullStatisticsDiferencial() {
        if (handlerData == null) return getEpsilonValue() + getDeltaValue() + emptyLineStats();

        return getEpsilonValue() + getDeltaValue() + getSupressedData() + getQualityAttributes() + getQualityRecords();
    }

}
