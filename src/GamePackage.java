import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePackage implements Serializable {
    private QuestionBank questionBank = new QuestionBank();
    private Subject chosenSubjectForRound;
    private int nrOfQuestions;

    public GamePackage(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }

    public int getNrOfQuestions() {
        return nrOfQuestions;
    }

    public void setNrOfQuestions(int nrOfQuestions) {
        this.nrOfQuestions = nrOfQuestions;
    }

    public Subject getChosenSubjectForRound() {
        return chosenSubjectForRound;
    }

    public void setChosenSubjectForRound(Subject chosenSubjectForRound) {
        this.chosenSubjectForRound = chosenSubjectForRound;
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
