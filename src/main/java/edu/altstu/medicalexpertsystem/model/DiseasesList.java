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
}
