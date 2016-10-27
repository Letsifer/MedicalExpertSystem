package edu.altstu.medicalexpertsystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Евгений
 */
public class RememberedAnswerList {
    private List<RememberedAnswer> list = new ArrayList<>();
    
    public void clearList() {
        list.clear();
    }
    
    public void addAnswer(String title, int answer) {
        list.add(new RememberedAnswer(title, answer));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Ваши ответы:\n");
        list.stream()
                .forEach(item -> builder.append(item));
        return builder.toString();
    }
}

class RememberedAnswer{
    private String title;
    private int answer;

    public RememberedAnswer(String title, int answer) {
        this.title = title;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "На вопрос " + title + " вы ответили " + answer + "\n";
    }
    
}
