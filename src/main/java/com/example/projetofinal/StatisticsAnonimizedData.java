package com.example.projetofinal;

import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.aggregates.StatisticsBuilder;
import org.deidentifier.arx.aggregates.quality.QualityMeasureColumnOriented;
import org.deidentifier.arx.risk.RiskEstimateBuilder;

public class StatisticsAnonimizedData {

    private final DataHandle handlerData;
    private final StatisticsBuilder statisticsBuilder;
    private final int kValue;

    public StatisticsAnonimizedData(DataHandle data, int kValue) {
        this.handlerData = data;
        this.statisticsBuilder = handlerData.getStatistics();
        this.kValue = kValue;
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
        QualityMeasureColumnOriented generalizationIntensity = statisticsBuilder.getQualityStatistics().getGeneralizationIntensity();
        QualityMeasureColumnOriented missings = statisticsBuilder.getQualityStatistics().getMissings();
        QualityMeasureColumnOriented entropy = statisticsBuilder.getQualityStatistics().getNonUniformEntropy();
        QualityMeasureColumnOriented squaredError = statisticsBuilder.getQualityStatistics().getAttributeLevelSquaredError();

        String result = "";

        for (String attribute : handlerData.getDefinition().getQuasiIdentifyingAttributes()) {
            result = result.concat(String.format("%.3f", generalizationIntensity.getValue(attribute) * 100) + ";" + String.format("%.3f", missings.getValue(attribute) * 100) + ";" + String.format("%.3f", entropy.getValue(attribute) * 100) + ";" + String.format("%.3f", squaredError.getValue(attribute) * 100) + "; ;");
        }

        return result;
    }

    // Function that returns a String with the row oriented measures
    public String getQualityRecords() {
        double discernibility = statisticsBuilder.getQualityStatistics().getDiscernibility().getValue() * 100;
        double averageClassSize = statisticsBuilder.getQualityStatistics().getAverageClassSize().getValue() * 100;
        double squaredError = statisticsBuilder.getQualityStatistics().getRecordLevelSquaredError().getValue() * 100;

        return String.format("%.3f", discernibility) + ";" + String.format("%.3f", averageClassSize) + ";" + String.format("%.3f", squaredError) + ";";
    }

    // Function that returns a String with the k value
    public String getKValue() {
        return kValue + ";";
    }

    // Function that returns a String with the risk measures
    public String getRiskMeasures() {
        RiskEstimateBuilder estimate = handlerData.getRiskEstimator();
        double prosecutorRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk() * 100;
        double journalistRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedJournalistRisk() * 100;
        double marketerRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedMarketerRisk() * 100;

        // Return  the K value and the risk measures (they should be in % with 2 decimal places)
        return getKValue() + String.format("%.3f", prosecutorRisk) + ";" + String.format("%.3f", journalistRisk) + ";" + String.format("%.3f", marketerRisk);
    }

    // Function that returns a String with all the statistics
    public String getFullStatistics() {
        return getKValue() + getSupressedData() + getQualityAttributes() + getQualityRecords();
    }

}
