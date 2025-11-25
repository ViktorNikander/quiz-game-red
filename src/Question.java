import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question implements Serializable {
    private String question;
    private String answer;
    private List<String> wrongAnswersList = new ArrayList<>();
    private List<String> allAnswers = new ArrayList<>();

    public Question(String question, String correctAnswer, String wrongAnswerOne, String wrongAnswerTwo, String wrongAnswerThree) {
        this.question = question;
        this.answer = correctAnswer;
        this.wrongAnswersList.add(wrongAnswerOne);
        this.wrongAnswersList.add(wrongAnswerTwo);
        this.wrongAnswersList.add(wrongAnswerThree);
        allAnswers.addAll(wrongAnswersList);
        allAnswers.add(answer);
    }

    public List<String> getAllAnswers() {
        return allAnswers;
    }

    public void setAllAnswers(List<String> allAnswers) {
        this.allAnswers = allAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getWrongAnswersList() {
        return wrongAnswersList;
    }

    public void setWrongAnswersList(List<String> wrongAnswersList) {
        this.wrongAnswersList = wrongAnswersList;
    }
}
