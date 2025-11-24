import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank implements Serializable {
    private List<Subject> subjectList = new ArrayList<>();

    public QuestionBank() {
        Question q1 = new Question("First?", "1", List.of("2", "3", "4"));
        Question q2 = new Question("Second?", "2", List.of("1", "3", "4"));
        Question q3 = new Question("Third?", "3", List.of("2", "1", "4"));
        Question q4 = new Question("Fourth?", "4", List.of("2", "3", "1"));
        Subject s1 = new Subject("First subject", List.of(q1, q2, q3, q4));
        Subject s2 = new Subject("Second subject", List.of(q1, q2, q3, q4));
        Subject s3 = new Subject("Third subject", List.of(q1, q2, q3, q4));
        Subject s4 = new Subject("Fourth subject", List.of(q1, q2, q3, q4));
        subjectList.add(s1);
        subjectList.add(s2);
        subjectList.add(s3);
        subjectList.add(s4);
    }

    public QuestionBank(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }
}
