package edu.altstu.medicalexpertsystem.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author gea
 */
@Getter
@Setter
public class MedicalDependency {

    private final Disease disease;

    private final Symptom symptom;

    private double diseaseWithSymptom;

    private double noDiseaseWithSymptom;

    public MedicalDependency(Disease disease, Symptom symptom) {
        this.disease = disease;
        this.symptom = symptom;
    }

    public MedicalDependency(Disease disease, Symptom symptom, double diseaseWithSymptom, double noDiseaseWithSymptom) {
        this.disease = disease;
        this.symptom = symptom;
        this.diseaseWithSymptom = diseaseWithSymptom;
        this.noDiseaseWithSymptom = noDiseaseWithSymptom;
    }

    public double diseaseWithNoSymptom() {
        return 1 - diseaseWithSymptom;
    }

    public double noDiseaseWithNoSymptom() {
        return 1 - noDiseaseWithSymptom;
    }

    public void updateDiseasesCurrentFrequense(Answer answer) {
        if (answer.getCurrentAnswer() == 0) {
            return;
        }
        double  old = disease.getCurrentFrequency(),
                reverseOld = 1 - old,
                k = answer.getCurrentAnswer(),
                apriori = disease.getAprioriFrequency();
        if (k > 0) {
            double maxPos = diseaseWithSymptom * old / (diseaseWithSymptom * old + noDiseaseWithSymptom * reverseOld);
            double newPos = (k * maxPos - apriori * (Answer.MAX_RANGE - k)) / Answer.MAX_RANGE;
            disease.setCurrentFrequency(newPos);
        } else {
            double minPos = diseaseWithNoSymptom() * old / (diseaseWithNoSymptom() * old + noDiseaseWithNoSymptom() * reverseOld);
            double newPos = (apriori * (Answer.MAX_RANGE + k) - k * minPos) / Answer.MAX_RANGE;
            disease.setCurrentFrequency(newPos);
        }
    }
}
