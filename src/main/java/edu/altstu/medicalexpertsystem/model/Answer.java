package edu.altstu.medicalexpertsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Евгений
 */
@Getter
@Setter
@NoArgsConstructor
public class Answer {
    public final static int MAX_RANGE = 5, MIN_RANGE = -5;
    
    private int currentAnswer;
}
