import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank implements Serializable {
    private List<Subject> subjectList = new ArrayList<>();

    public QuestionBank() {
        Question q1 = new Question("First?", "1", "2", "3", "4");
        Question q2 = new Question("Second?", "2", "1", "3", "4");
        Question q3 = new Question("Third?", "3", "2", "1", "4");
        Question q4 = new Question("Fourth?", "4", "2", "3", "1");
        List<Question> questionList = new ArrayList<>();
        questionList.add(q1);
        questionList.add(q2);
        questionList.add(q3);
        questionList.add(q4);
        Subject s1 = new Subject("First subject", questionList);
        Subject s2 = new Subject("Second subject", questionList);
        Subject s3 = new Subject("Third subject", questionList);
        Subject s4 = new Subject("Fourth subject", questionList);
        subjectList.add(s1);
        subjectList.add(s2);
        subjectList.add(s3);
        subjectList.add(s4);
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }
}
