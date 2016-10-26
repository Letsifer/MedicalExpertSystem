package edu.altstu.medicalexpertsystem;

import edu.altstu.medicalexpertsystem.model.Answer;
import edu.altstu.medicalexpertsystem.model.Disease;
import edu.altstu.medicalexpertsystem.model.DiseasesList;
import edu.altstu.medicalexpertsystem.model.Symptom;
import edu.altstu.medicalexpertsystem.model.SymptomsList;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable {

    @FXML
    private Label questionLabel;
    @FXML
    private TextField questionTextField;
    private String currentAnswer = NO_ANSWER;
    private final static String NO_ANSWER = "Текущий ответ: \n",
            FIND_ANSWER = "Итоговый ответ: \n";

    @FXML
    private Label userAnswerLabel;
    @FXML
    private ComboBox<Integer> userAnswerComboBox;
    @FXML
    private Button userAnswerButton;

    @FXML
    public void userAnswerButtonClick() {
        answerTextArea.setText("");
        userAnswer.setCurrentAnswer(userAnswerComboBox.getValue());
        Disease possible = diseasesList.updateAndFindCurrentMaxPossible(currentSymptom, userAnswer);
        boolean isOneDiseaseLeft = diseasesList.updateRightDiseases();
        boolean noQuestionsShouldBeAsked = symptomsList.updateSymptomsPossibleDiseases();
        if (isOneDiseaseLeft || noQuestionsShouldBeAsked) {
            currentAnswer = FIND_ANSWER;
            List<Disease> leastDiseases = diseasesList.getLeastDiseases();
            Collections.sort(leastDiseases,
                    (Disease o1, Disease o2) -> Double.compare(o1.getCurrentFrequency(), o2.getCurrentFrequency())
            );
            StringBuilder stB = new StringBuilder(currentAnswer);
            leastDiseases.stream().forEach((disease) -> {
                stB.append(disease.getTitle())
                        .append(" с вероятностью ")
                        .append(disease.getCurrentFrequency())
                        .append("\n ");
            });
            answerTextArea.setText(stB.toString());
            userAnswerButton.setDisable(true);
            questionTextField.setText("Вопросов не осталось");
        } else {
            StringBuilder stB = new StringBuilder(currentAnswer);
            stB.append(possible.getTitle())
                    .append(" с вероятностью ")
                    .append(possible.getCurrentFrequency())
                    .append("\n ");
            answerTextArea.setText(stB.toString());
            currentSymptom = symptomsList.getMostSignificantQuestion();
            questionTextField.setText(currentSymptom.getTitle());
        }
    }

    @FXML
    private TextArea answerTextArea;

    @FXML
    private Button reloadButton;
    @FXML
    public void reloadButtonClick() {
        diseasesList.initObject();
        symptomsList.initObject();
        currentAnswer = NO_ANSWER;
        userAnswerButton.setDisable(false);
    }

    private Answer userAnswer;
    private Symptom currentSymptom;
    private DiseasesList diseasesList;
    private SymptomsList symptomsList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userAnswer = new Answer();
        diseasesList = new DiseasesList();
        symptomsList = new SymptomsList();
        //read all data
        for (int i = Answer.MIN_RANGE; i <= Answer.MAX_RANGE; i++) {
            
        }        
        userAnswerComboBox.setItems(
                FXCollections.observableArrayList(
                        IntStream.rangeClosed(Answer.MIN_RANGE, Answer.MAX_RANGE)
                                .boxed()
                                .collect(Collectors.toList())
                )
        );
        currentSymptom = symptomsList.getMostSignificantQuestion();
        questionTextField.setText(currentSymptom.getTitle());
    }
}
