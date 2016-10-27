package edu.altstu.medicalexpertsystem.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Евгений
 */
public class SymptomsList extends MedicalObject<Integer>{

    private List<Symptom> symptoms;

    public void addSymptom(Symptom symptom) {
        symptoms.add(symptom);
    }
    
    public int getSymptomsNumber() {
        return symptoms.size();
    }
    
    public Symptom getSymptomWithId(Integer id) {
        return symptoms.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public SymptomsList() {
        super(0);
        symptoms = new ArrayList<>();
    }

    @Override
    public void initObject() {
        symptoms
                .stream()
                .forEach(s -> s.initObject());
    }

    public Symptom getMostSignificantQuestion() {
        Symptom question = symptoms.stream()
                .filter(s -> s.isShouldBeAsked())
                .max(Comparator.comparing(Symptom::getPossibleDiseases))
                .get();
        question.setShouldBeAsked(false);
        return question;
    }

    public boolean updateSymptomsPossibleDiseases() {
        return symptoms.stream()
                .filter(s -> s.isShouldBeAsked())
                .peek(s -> s.recountPossibleDiseases())
                .mapToInt(s -> s.getPossibleDiseases() > 0 ? 1 : 0)
                .sum() == 0;                
    }
}
