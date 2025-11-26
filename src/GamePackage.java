import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePackage implements Serializable {
    private QuestionBank questionBank = new QuestionBank();
    private Subject chosenSubjectForRound;
    private int nrOfQuestions;
    private int nrOfSubjects;
    private int nrOfRounds;
    private String gameState;
    private boolean isFirstPlayerOnSubject = true;
    private int indexRoundsPlayed = 0;

    public GamePackage(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }

    public void shuffleAll(){
        Collections.shuffle(questionBank.getSubjectList());
        for (Subject subject : questionBank.getSubjectList()){
            Collections.shuffle(subject.getQuestionList());
        }
    }

    public int getNrOfRounds() {
        return nrOfRounds;
    }

    public void setNrOfRounds(int nrOfRounds) {
        this.nrOfRounds = nrOfRounds;
    }

    public int getIndexRoundsPlayed() {
        return indexRoundsPlayed;
    }

    public void setIndexRoundsPlayed(int indexRoundsPlayed) {
        this.indexRoundsPlayed = indexRoundsPlayed;
    }

    public boolean isFirstPlayerOnSubject() {
        return isFirstPlayerOnSubject;
    }

    public void setFirstPlayerOnSubject(boolean firstPlayerOnSubject) {
        isFirstPlayerOnSubject = firstPlayerOnSubject;
    }

    public int getNrOfSubjects() {
        return nrOfSubjects;
    }

    public void setNrOfSubjects(int nrOfSubjects) {
        this.nrOfSubjects = nrOfSubjects;
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

    public void setGameState(String choose) {
        this.gameState = choose;
    }

    public String getGameState() {
        return gameState;
    }
}
