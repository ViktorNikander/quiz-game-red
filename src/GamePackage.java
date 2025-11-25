import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePackage implements Serializable {
    QuestionBank questionBank = new QuestionBank();

    public GamePackage(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }

    public QuestionBank getQuestionBank() {
        return questionBank;
    }

    public void setQuestionBank(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }

    public void shuffleAll(){
        Collections.shuffle(questionBank.getSubjectList());
        for (Subject subject : questionBank.getSubjectList()){
            Collections.shuffle(subject.getQuestionList());
        }
    }
}
