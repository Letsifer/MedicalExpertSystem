package edu.altstu.medicalexpertsystem.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author gea
 */
@Getter
@Setter
public class Symptom extends MedicalObject<Integer>{
    private final String title;

    private boolean shouldBeAsked = true;
    
    public Symptom(Integer id, String title) {
        super(id);
        this.title = title;
    }

    @Override
    public void initObject() {
        shouldBeAsked = true;
    }  
    
}
