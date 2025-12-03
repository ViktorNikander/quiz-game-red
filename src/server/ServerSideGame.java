package server;

import match.history.MatchHistory;
import questions.QuestionBank;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ServerSideGame extends Thread{
    private ServerSidePlayer firstPlayer;
    private ServerSidePlayer secondPlayer;
    private ServerSidePlayer currentPlayer;
    private int nrOfQuestions;
    private int nrOfRounds;
    private int nrOfSubjects;
    GamePackage gamePackage;

    public ServerSideGame(ServerSidePlayer firstPlayer, ServerSidePlayer secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.currentPlayer = firstPlayer;
        Properties p = new Properties();

        try{
            p.load(new FileInputStream("src/GameSettings.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        nrOfQuestions = Integer.parseInt(p.getProperty("questionsPerRound", "2"));
        nrOfRounds = Integer.parseInt(p.getProperty("roundsPerGame", "2"));
        nrOfSubjects = Integer.parseInt(p.getProperty("subjectsPerChoice"));
        gamePackage = new GamePackage(new QuestionBank(), currentPlayer.getPlayer());
        gamePackage.setNrOfQuestions(nrOfQuestions);
        gamePackage.setNrOfSubjects(nrOfSubjects);
        gamePackage.setNrOfRounds(nrOfRounds);
        gamePackage.setMatchHistory(new MatchHistory(nrOfRounds, nrOfQuestions));
    }

    @Override
    public void run() {
        String gameState = "subject";
        gamePackage.setGameState(gameState);
        gamePackage.shuffleAll();
        updateScore();
        currentPlayer.send(gamePackage);
        while ((gamePackage = (GamePackage) currentPlayer.receive()) != null){
            int result = gamePackage.getLastAnswerCorrect();
            if (result == 1) {
                currentPlayer.addCorrectAnswer();
                currentPlayer.addScorePoint();
            }else if (result == 0) {
                currentPlayer.addWrongAnswer();
            }

            gamePackage.setLastAnswerCorrect(-1);

            gameState = gamePackage.getGameState();
            if (gameState.equalsIgnoreCase("subject")){
                changeCurrentPlayer();
                gamePackage.shuffleAll();
                gamePackage.setGameState("subject");
                updateScore();
                currentPlayer.send(gamePackage);
            } else if (gameState.equalsIgnoreCase("question")) {
                updateScore();
                currentPlayer.send(gamePackage);
            } else if (gameState.equalsIgnoreCase("switch")) {
                changeCurrentPlayer();
                gamePackage.setGameState("question");
                updateScore();
                currentPlayer.send(gamePackage);
            } else if (gameState.equalsIgnoreCase("send update")) {
                changeCurrentPlayer();
                gamePackage.setGameState("get update");
                currentPlayer.send(gamePackage);
            } else if (gameState.equalsIgnoreCase("quit")) {
                String resultMsg = "Game over!\n" + "Player 1 - Correct: " + firstPlayer.getCorrectAnswers()
                        + ", Incorrect: " + firstPlayer.getWrongAnswers() + "\n" + "Player 2 - Correct: " + secondPlayer.getCorrectAnswers()
                        + ", Incorrect: " + secondPlayer.getWrongAnswers();
                gamePackage.setFinalResultMessage(resultMsg);
                updateScore();
                firstPlayer.send(gamePackage);
                secondPlayer.send(gamePackage);
                System.out.println(resultMsg);
                System.out.println("quit");
            }
        }
    }
    private void updateScore() {
        gamePackage.setFirstPlayerScore(firstPlayer.getScore());
        gamePackage.setSecondPlayerScore(secondPlayer.getScore());
    }

    private void changeCurrentPlayer() {
        if (currentPlayer == firstPlayer){
            currentPlayer = secondPlayer;
            gamePackage.setCurrentPlayer(currentPlayer.getPlayer());
        }else {
            currentPlayer = firstPlayer;
            gamePackage.setCurrentPlayer(currentPlayer.getPlayer());
        }
    }
}
