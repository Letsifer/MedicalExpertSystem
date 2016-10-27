package edu.altstu.medicalexpertsystem;

import edu.altstu.medicalexpertsystem.model.Answer;
import edu.altstu.medicalexpertsystem.model.Disease;
import edu.altstu.medicalexpertsystem.model.DiseasesList;
import edu.altstu.medicalexpertsystem.model.MedicalDependency;
import edu.altstu.medicalexpertsystem.model.Symptom;
import edu.altstu.medicalexpertsystem.model.SymptomsList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
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

    private final String symptomsFileName = "src//main//resources//symptoms.txt";
    private final String diseasesFileName = "src//main//resources//diseases.txt";

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
        list.addAnswer(currentSymptom.getTitle(), userAnswer.getCurrentAnswer());
        Disease possible = diseasesList.updateAndFindCurrentMaxPossible(currentSymptom, userAnswer);
        boolean isOneDiseaseLeft = diseasesList.updateRightDiseases();
        boolean noQuestionsShouldBeAsked = symptomsList.updateSymptomsPossibleDiseases();
        if (isOneDiseaseLeft || noQuestionsShouldBeAsked) {
            currentAnswer = FIND_ANSWER;
            List<Disease> leastDiseases = diseasesList.getLeastDiseases();
            Collections.sort(leastDiseases,
                    (Disease o1, Disease o2) -> Double.compare(o2.getCurrentFrequency(), o1.getCurrentFrequency())
            );
            StringBuilder stB = new StringBuilder(currentAnswer);
            leastDiseases.stream().forEach((disease) -> {
                stB.append(disease.getTitle())
                        .append(" с вероятностью ")
                        .append(disease.getCurrentFrequency())
                        .append("\n\n ");
            });
            stB.append(list);
            answerTextArea.setText(stB.toString());
            userAnswerButton.setDisable(true);
            questionTextField.setText("Вопросов не осталось");
        } else {
            StringBuilder stB = new StringBuilder(currentAnswer);
            stB.append(possible.getTitle())
                    .append(" с вероятностью ")
                    .append(possible.getCurrentFrequency())
                    .append("\n\n ");
            stB.append(list);
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
        currentSymptom = symptomsList.getMostSignificantQuestion();
        questionTextField.setText(currentSymptom.getTitle());
        answerTextArea.setText("");
        list.clearList();
    }

    private RememberedAnswerList list = new RememberedAnswerList();
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
        inputSymptomps();
        inputDiseasesInfo();
        diseasesList.initObject();
        symptomsList.initObject();
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

    private void inputSymptomps() {
        try {
            Inputer symptomsInputer = new Inputer(symptomsFileName);
            int count = 1;
            while (!symptomsInputer.isEOF()) {
                String title = symptomsInputer.readLine();
                Symptom symptom = new Symptom(count++, title);
                symptomsList.addSymptom(symptom);
                System.err.println(symptom);
            }
            System.err.println("-------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inputDiseasesInfo() {
        try {
            Inputer diseasesInputer = new Inputer(diseasesFileName);
            System.err.println(diseasesInputer.nextToken());
            int diseasesNumber = diseasesInputer.nextInt();
            for (int i = 1; i <= diseasesNumber; i++) {
                String title = diseasesInputer.readLine();
                double p0 = diseasesInputer.nextDouble();
                Disease disease = new Disease(i, title, p0);
                System.err.println(disease);
                int symptomsNumber = diseasesInputer.nextInt();
                for (int j = 0; j < symptomsNumber; j++) {
                    int id = diseasesInputer.nextInt();
                    Symptom symptom = symptomsList.getSymptomWithId(id);
                    MedicalDependency dependency = new MedicalDependency(disease, symptom, diseasesInputer.nextDouble(), diseasesInputer.nextDouble());
                    disease.addDependency(dependency);
                    symptom.addDependency(dependency);
                    System.err.println(dependency);
                }
                diseasesList.addDisease(disease);
                System.err.println("=====================================================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Inputer {

        BufferedReader in;
        StringTokenizer st = new StringTokenizer("");
        private String temp;

        public Inputer(String fileName) throws IOException {
            in = new BufferedReader(
                    new InputStreamReader(
                            new DataInputStream(new FileInputStream(fileName)),
                            "utf-8")
            );
        }

        public String nextToken() throws Exception {
            while (!st.hasMoreTokens()) {
                st = new StringTokenizer(readLine());
            }
            return st.nextToken();
        }

        public Integer nextInt() throws Exception {
            return Integer.parseInt(nextToken());
        }

        public Double nextDouble() throws Exception {
            return Double.parseDouble(nextToken());
        }

        public String readLine() throws IOException {
            if (temp != null) {
                String temp2 = temp;
                temp = null;
                return temp2;
            }
            return in.readLine();
        }

        public boolean isEOF() throws IOException {
            temp = in.readLine();
            return temp == null;
        }

    }
}
