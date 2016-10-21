package edu.altstu.medicalexpertsystem.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author gea
 */
@Getter
@Setter
public class Disease extends MedicalObject{
    private final String title;
    
    private final double aprioriFrequency;
    
    private boolean canBeRightDisease = true;
    
    private double currentFrequency;
    
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
    
}
