import java.util.ArrayList;
import java.util.List;

public class GamePackage {
    private List<Subject> subjectAlternatives = new ArrayList<>();

    public List<Subject> getSubjectAlternatives() {
        return subjectAlternatives;
    }

    public void setSubjectAlternatives(List<Subject> subjectAlternatives) {
        this.subjectAlternatives = subjectAlternatives;
    }
}
