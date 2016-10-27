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

    @Override
    public String toString() {
        return "MedicalDependency{" + "disease=" + disease.getTitle() + ", symptom=" + symptom.getTitle() + ", diseaseWithSymptom=" + diseaseWithSymptom + ", noDiseaseWithSymptom=" + noDiseaseWithSymptom + '}';
    }
    
    public MedicalDependency(Disease disease, Symptom symptom, double diseaseWithSymptom, double noDiseaseWithSymptom) {
        this(disease, symptom);
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
        double k = answer.getCurrentAnswer();
        if (k > 0) {
            double newPos = countMaxPossibility(k);
            disease.setCurrentFrequency(newPos);
        } else {
            double newPos = countMinPossibility(k);
            disease.setCurrentFrequency(newPos);
        }
    }

    public double countMaxPossibility(double k) {
        double old = disease.getCurrentFrequency();
        double reverseOld = 1 - old,
                apriori = disease.getAprioriFrequency();
        double maxPos = diseaseWithSymptom * old / (diseaseWithSymptom * old + noDiseaseWithSymptom * reverseOld);
        double pos = (k * maxPos - apriori * (Answer.MAX_RANGE - k)) / Answer.MAX_RANGE;
        return pos;
    }

    public double countMinPossibility(double k) {
        double old = disease.getCurrentFrequency();
        double reverseOld = 1 - old,
                apriori = disease.getAprioriFrequency();
        double minPos = diseaseWithNoSymptom() * old / (diseaseWithNoSymptom() * old + noDiseaseWithNoSymptom() * reverseOld);
        double pos = (apriori * (Answer.MAX_RANGE + k) - k * minPos) / Answer.MAX_RANGE;
        return pos;
    }
}
