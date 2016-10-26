package edu.altstu.medicalexpertsystem.model;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Евгений
 */
public class DiseasesList {
    private List<Disease> diseases;
    
    public Disease updateAndFindCurrentMaxPossible(Symptom symptom, Answer answer) {
        symptom.updateDiseasesPosterioriPossibilities(answer);
        return diseases.stream()
                .filter(disease -> disease.isCanBeRightDisease())
                .max(Comparator.comparing(Disease::getCurrentFrequency))
                .get();
    }
    
    public boolean updateRightDiseases() {
        int leastDeseasesNumber = diseases.stream()
                .mapToInt(d -> d.isCanBeRightDisease() ? 1 : 0)
                .sum();
        int count = 0;
        double[][] borders = new double[leastDeseasesNumber][2];
        for (Disease disease : diseases) {
            if (disease.isCanBeRightDisease()) {
                borders[count++] = disease.getMinAndMaxPossibilities();
            }
        }
        for (int i = 0; i < leastDeseasesNumber; i++) {
            Disease checkedDisease = diseases.get(i);
            for (int j = 0; j < leastDeseasesNumber; j++) {
                if (Double.compare(borders[i][0], borders[j][1]) < 0) {
                    checkedDisease.setCanBeRightDisease(false);
                    break;
                }
            }
        }
        return diseases.stream()
                .mapToInt(d -> d.isCanBeRightDisease() ? 1 : 0)
                .sum() > 1;
    }
}
