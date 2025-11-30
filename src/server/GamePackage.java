package server;

import match.history.MatchHistory;
import questions.Question;
import questions.QuestionBank;
import questions.Subject;

import java.io.Serializable;
import java.util.Collections;

public class GamePackage implements Serializable {
    private ServerSidePlayer firstPlayer;
    private ServerSidePlayer secondPlayer;
    private String currentPlayer;
    private QuestionBank questionBank = new QuestionBank();
    private Subject chosenSubjectForRound;
    private int nrOfQuestions;
    private int nrOfSubjects;
    private int nrOfRounds;
    private String gameState;
    private boolean isFirstPlayerOnSubject = true;
    private int indexRoundsPlayed = 0;
    private int lastAnswerCorrect = -1;
    private MatchHistory matchHistory;

    public GamePackage(QuestionBank questionBank, String currentPlayer) {
        this.questionBank = questionBank;
        this.currentPlayer = currentPlayer;
    }

    public MatchHistory getMatchHistory() {
        return matchHistory;
    }

    public boolean isFirstPlayerOnSubject() {
        return isFirstPlayerOnSubject;
    }

    public void setMatchHistory(MatchHistory matchHistory) {
        this.matchHistory = matchHistory;
    }

    public int getLastAnswerCorrect() {
        return lastAnswerCorrect;
    }
    public void setLastAnswerCorrect(int lastAnswerCorrect) {
        this.lastAnswerCorrect = lastAnswerCorrect;
    }

    public void shuffleAll(){
        Collections.shuffle(questionBank.getSubjectList());
        for (Subject subject : questionBank.getSubjectList()){
            Collections.shuffle(subject.getQuestionList());
            for (Question question : subject.getQuestionList()){
                Collections.shuffle(question.getAllAnswers());
            }
        }
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
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

    public void setGameState(String choose) {
        this.gameState = choose;
    }

    public String getGameState() {
        return gameState;
    }
}
