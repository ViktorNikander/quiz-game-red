import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class ServerSideGame extends Thread{
    private ServerSidePlayer firstPlayer;
    private ServerSidePlayer secondPlayer;
    private ServerSidePlayer currentPlayer;
    private QuestionBank questionBank = new QuestionBank();
    GamePackage gamePackage = new GamePackage(new QuestionBank());
    private int nrOfQuestions;
    private int nrOfRounds;
    private Object outgoing;
    private Object incoming;

    public ServerSideGame(ServerSidePlayer firstPlayer, ServerSidePlayer secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.currentPlayer = firstPlayer;
        this.firstPlayer.setOpponent(secondPlayer);
        this.secondPlayer.setOpponent(firstPlayer);

        Properties p = new Properties();

        try{
            p.load(new FileInputStream("src/GameSettings.properties"));
            nrOfQuestions = Integer.parseInt(p.getProperty("questionsPerRound", "2"));
            nrOfRounds = Integer.parseInt(p.getProperty("roundsPerGame", "2"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.nrOfQuestions = Integer.parseInt(p.getProperty("questionsPerRound", "2"));
        this.nrOfRounds = Integer.parseInt(p.getProperty("roundsPerGame", "2"));
    }

    @Override
    public void run() {
        for (int i = 0; i < nrOfRounds; i++) {
            oneRound();
        }
    }

    private void oneRound() {
        Collections.shuffle(questionBank.getSubjectList());
        currentPlayer.send();
        currentPlayer.receive();
        /*
        Shuffle subjects
        Send three subjects to current player
        Receive chosen subject from current player
        Store chosen subject
        Shuffle questions of chosen subject
        Store #nrOfQuestions on server side
        Send #nrOfQuestions to current player
        Receive and store score
        Change current player
        Send Subject and questions to current player
        Receive and store score
         */
    }
}
