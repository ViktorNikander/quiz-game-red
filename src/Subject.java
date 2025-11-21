import java.util.List;

public class Subject {
    private String subject;
    private List<Question> questionList;

    public Subject() {
    }

    public Subject(String subject) {
        this.subject = subject;
    }

    public Subject(String subject, List<Question> questionList) {
        this.subject = subject;
        this.questionList = questionList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
