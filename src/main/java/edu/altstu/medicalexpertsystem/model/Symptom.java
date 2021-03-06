package edu.altstu.medicalexpertsystem.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author gea
 */
@Getter
@Setter
public class Symptom extends MedicalObject<Integer> {

    public void updateDiseasesPosterioriPossibilities(Answer answer) {
        dependencies
                .stream()
                .filter(dep -> dep.getDisease().isCanBeRightDisease())
                .forEach(dep -> dep.updateDiseasesCurrentFrequense(answer));
    }

    private final String title;

    private boolean shouldBeAsked = true;

    @Setter(AccessLevel.NONE)
    private int possibleDiseases = 0;
    private List<MedicalDependency> dependencies = new ArrayList<>();

    public void addDependency(MedicalDependency dependency) {
        dependencies.add(dependency);
        recountPossibleDiseases();
    }
    
    public void setDiseases(List<MedicalDependency> dependencies) {
        this.dependencies.addAll(dependencies);
        recountPossibleDiseases();
    }

    public void recountPossibleDiseases() {
        possibleDiseases = this.dependencies
                .stream()
                .map(dep -> dep.getDisease())
                .mapToInt(dis -> dis.isCanBeRightDisease() ? 1 : 0)
                .sum();
        if (possibleDiseases == 0) {
            shouldBeAsked = false;
        }
    }

    public Symptom(Integer id, String title) {
        super(id);
        this.title = title;
    }

    public void addDisease(MedicalDependency dependency) {
        dependencies.add(dependency);
        if (dependency.getDisease().isCanBeRightDisease()) {
            possibleDiseases++;
        }
    }

    @Override
    public void initObject() {
        shouldBeAsked = true;
        recountPossibleDiseases();
    }

    @Override
    public String toString() {
        return "Symptom{" + title + '}';
    }
}
