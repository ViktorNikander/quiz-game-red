import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamePackage implements Serializable {
    private List<Subject> subjectAlternatives = new ArrayList<>();

    public List<Subject> getSubjectAlternatives() {
        return subjectAlternatives;
    }

    public void setSubjectAlternatives(List<Subject> subjectAlternatives) {
        this.subjectAlternatives = subjectAlternatives;
    }
}
