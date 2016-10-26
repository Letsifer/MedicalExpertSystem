package edu.altstu.medicalexpertsystem.model;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Евгений
 */
public class SymptomsList extends MedicalObject<Integer>{

    private List<Symptom> symptoms;

    public SymptomsList() {
        super(0);
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
        symptoms.stream()
                .filter(s -> s.isShouldBeAsked())
                .forEach(s -> s.recountPossibleDiseases());
        return symptoms.stream()
                .filter(s -> s.isShouldBeAsked())
                .mapToInt(s -> s.getPossibleDiseases() > 0 ? 1 : 0)
                .sum() > 0;
    }
}
