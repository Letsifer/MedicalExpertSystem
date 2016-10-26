package edu.altstu.medicalexpertsystem.model;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Евгений
 */
public class SymptomsList {
    private List<Symptom> symptoms;
    private static final Random rand = new Random();
    
    public Symptom getMostSignificantQuestion() {
        Symptom question = symptoms.stream()
                .filter(s -> s.isShouldBeAsked())
                .max(Comparator.comparing(Symptom::getPossibleDiseases))
                .get();
        question.setShouldBeAsked(false);
        return question;
    }
}
