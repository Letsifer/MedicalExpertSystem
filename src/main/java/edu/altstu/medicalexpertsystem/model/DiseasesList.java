package edu.altstu.medicalexpertsystem.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Евгений
 */
public class DiseasesList extends MedicalObject<Integer> {

    private List<Disease> diseases;

    public DiseasesList() {
        super(0);
    }

    @Override
    public void initObject() {
        diseases
                .stream()
                .forEach(d -> d.initObject());
    }

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

    public List<Disease> getLeastDiseases() {
        return diseases.stream()
                .filter(d -> d.isCanBeRightDisease())
                .collect(Collectors.toList());
    }
}
