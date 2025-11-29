import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subject implements Serializable {
    private String subject;
    private List<Question> questionList = new ArrayList<>();

    public Subject(String subject, List<Question> questionList) {
        this.subject = subject;
        this.questionList = questionList;
    }

    public String getSubject() {
        return subject;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }
}
