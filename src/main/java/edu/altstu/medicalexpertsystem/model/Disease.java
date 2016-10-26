package edu.altstu.medicalexpertsystem.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;

/**
 *
 * @author gea
 */
@Getter
@Setter
public class Disease extends MedicalObject {

    private final String title;

    private final double aprioriFrequency;

    private boolean canBeRightDisease = true;

    private double currentFrequency;

    private List<MedicalDependency> dependencies = new ArrayList<>();

    public Disease(Integer id, String title) {
        super(id);
        this.title = title;
        this.aprioriFrequency = 0;
    }

    public Disease(Integer id, String title, double frequency) {
        super(id);
        this.title = title;
        this.aprioriFrequency = frequency;
    }

    @Override
    public void initObject() {
        canBeRightDisease = true;
        currentFrequency = aprioriFrequency;
    }

    public double[] getMinAndMaxPossibilities() {
        double maxValue = currentFrequency,
                minValue = currentFrequency;
        for (MedicalDependency dep : dependencies) {
            if (dep.getSymptom().isShouldBeAsked()) {
                double maxPosForMaxAns = dep.countMaxPossibility(Answer.MAX_RANGE),
                        maxPosForMinAns = dep.countMaxPossibility(Answer.MIN_RANGE),
                        minPosForMaxAns = dep.countMinPossibility(Answer.MAX_RANGE),
                        minPosForMinAns = dep.countMinPossibility(Answer.MIN_RANGE);
                maxValue = findMaxValue(maxValue, maxPosForMaxAns, maxPosForMinAns, minPosForMaxAns, minPosForMinAns);
                minValue = findMinValue(minValue, maxPosForMaxAns, maxPosForMinAns, minPosForMaxAns, minPosForMinAns);
            }
        }
        return new double[]{maxValue, minValue};
    }

    private double findMaxValue(double... values) {
        return Arrays.stream(values)
                .max()
                .getAsDouble();
    }

    private double findMinValue(double... values) {
        return Arrays.stream(values)
                .min()
                .getAsDouble();
    }

}
