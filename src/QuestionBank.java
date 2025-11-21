import java.util.List;

public class QuestionBank {
    private List<Subject> subjectList;

    public QuestionBank() {
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
