import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RoundHistory extends JLabel {
    private String subject;
    private List<Boolean> answerHistory = new ArrayList<>();

    public RoundHistory(){
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        JLabel subjectOfRound = new JLabel(getSubject());
        add(subjectOfRound);
    }

    public List<Boolean> getAnswerHistory() {
        return answerHistory;
    }

    public void setAnswerHistory(List<Boolean> answerHistory) {
        this.answerHistory = answerHistory;
    }

    public void addAnswerHistory(boolean isCorrect){
        answerHistory.add(isCorrect);
    }
}
