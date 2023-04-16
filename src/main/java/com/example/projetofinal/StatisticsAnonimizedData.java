package com.example.projetofinal;

import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.aggregates.StatisticsBuilder;
import org.deidentifier.arx.aggregates.quality.QualityMeasureColumnOriented;
import org.deidentifier.arx.aggregates.quality.QualityMeasureRowOriented;
import org.deidentifier.arx.risk.RiskEstimateBuilder;

public class StatisticsAnonimizedData {

    private final DataHandle handlerData;
    private final StatisticsBuilder statisticsBuilder;

    public StatisticsAnonimizedData(DataHandle data) {
        this.handlerData = data;
        this.statisticsBuilder = handlerData.getStatistics();
    }

    // Function that returns the number of rows in the dataset without considering the suppressed data
    public int getSupressedData() {
        int dadosSuprimidos = 0;
        for (int i = 0; i < handlerData.getNumRows(); i++) {
            if (handlerData.isSuppressed(i)) {
                dadosSuprimidos++;
            }
        }
        return dadosSuprimidos;
    }

    // Function that receives the array of quasi-identifiers and returns a String with the column oriented measures
    public String getQualityAttributes() {
        QualityMeasureColumnOriented generalizationIntensity = statisticsBuilder.getQualityStatistics().getGeneralizationIntensity();
        QualityMeasureColumnOriented missings = statisticsBuilder.getQualityStatistics().getMissings();
        QualityMeasureColumnOriented entropy = statisticsBuilder.getQualityStatistics().getNonUniformEntropy();
        QualityMeasureColumnOriented squaredError = statisticsBuilder.getQualityStatistics().getAttributeLevelSquaredError();

        String result = "";

        for (String ignored : handlerData.getDefinition().getQuasiIdentifyingAttributes()) {
            result = result.concat(generalizationIntensity + ";" + missings + ";" + entropy + ";" + squaredError + ";");
        }

        return result;
    }

    // Function that returns a String with the row oriented measures
    public String getQualityRecords() {
        QualityMeasureRowOriented discernibility = statisticsBuilder.getQualityStatistics().getDiscernibility();
        QualityMeasureRowOriented averageClassSize = statisticsBuilder.getQualityStatistics().getAverageClassSize();
        QualityMeasureRowOriented squaredError = statisticsBuilder.getQualityStatistics().getRecordLevelSquaredError();

        return discernibility.getValue() + ";" + averageClassSize.getValue() + ";" + squaredError.getValue() + ";";
    }

    // Function that returns a String with the risk measures
    public String getRiskMeasures() {
        RiskEstimateBuilder estimate = handlerData.getRiskEstimator();
        double prosecutorRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk();
        double journalistRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedJournalistRisk();
        double marketerRisk = estimate.getSampleBasedReidentificationRisk().getEstimatedMarketerRisk();

        return prosecutorRisk + ";" + journalistRisk + ";" + marketerRisk;
    }

    // Function that returns a String with all the statistics
    public String getFullStatistics() {
        return getSupressedData() + ";" + getQualityAttributes() + getQualityRecords();
    }

}
