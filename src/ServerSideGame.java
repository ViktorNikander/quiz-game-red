import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class ServerSideGame extends Thread{
    private ServerSidePlayer firstPlayer;
    private ServerSidePlayer secondPlayer;
    private ServerSidePlayer currentPlayer;
    GamePackage gamePackage = new GamePackage(new QuestionBank());
    private int nrOfQuestions;
    private int nrOfRounds;
    private int nrOfSubjects;

    public ServerSideGame(ServerSidePlayer firstPlayer, ServerSidePlayer secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.currentPlayer = firstPlayer;
        this.firstPlayer.setOpponent(secondPlayer);
        this.secondPlayer.setOpponent(firstPlayer);

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
        gamePackage.setNrOfQuestions(nrOfQuestions);
        gamePackage.setNrOfSubjects(nrOfSubjects);
    }

    @Override
    public void run() {
        for (int i = 0; i < nrOfRounds; i++) {
            oneRound();
        }
    }

    private void oneRound() {
        gamePackage.shuffleAll();
        currentPlayer.send(gamePackage);
        while ((gamePackage = (GamePackage) currentPlayer.receive()) != null){

        }
        System.out.println("passed while loop");
        changeCurrentPlayer();
        currentPlayer.send(gamePackage);
        gamePackage = (GamePackage) currentPlayer.receive();
        gamePackage.setChosenSubjectForRound(null);
        /*
        Shuffle all in game package
        Send game package to current player
        Store #nrOfQuestions on server side
        Send #nrOfQuestions to current player
        Receive and store score
        Change current player
        Send Subject and questions to current player
        Receive and store score
         */
    }

    private void changeCurrentPlayer() {
        if (currentPlayer == firstPlayer){
            currentPlayer = secondPlayer;
        }else {
            currentPlayer = firstPlayer;
        }
    }
}
