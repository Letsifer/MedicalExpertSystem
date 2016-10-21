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
}
