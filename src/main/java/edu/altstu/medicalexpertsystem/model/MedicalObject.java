package edu.altstu.medicalexpertsystem.model;

import lombok.Getter;

/**
 *
 * @author gea
 */
@Getter
public abstract class MedicalObject<T> {
    final T id;

    public MedicalObject(T id) {
        this.id = id;
    }
    
    void initObject() {
        
    }
}
