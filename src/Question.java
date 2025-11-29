import java.io.Serializable;
import java.util.ArrayList;
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

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
