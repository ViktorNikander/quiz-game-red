import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ServerSideGame extends Thread{
    private ServerSidePlayer firstPlayer;
    private ServerSidePlayer secondPlayer;
    private ServerSidePlayer currentPlayer;
    private QuestionBank questionBank = new QuestionBank();
    private int nrOfQuestions;
    private int nrOfRounds;

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

        this.nrOfQuestions = Integer.parseInt(p.getProperty("questionsPerRound", "2"));
        this.nrOfRounds = Integer.parseInt(p.getProperty("roundsPerGame", "2"));
    }

    @Override
    public void run() {
        System.out.println("Two clients connected");
        //TODO game logic
    }
}
