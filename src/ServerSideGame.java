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
        System.out.println("game with two players started");
        //TODO game logic
        int test = 0;
        outgoing = test;
        System.out.println("value is at: " + outgoing);
        currentPlayer.send(outgoing);
        System.out.println("sent value");
        while ((incoming = currentPlayer.receive()) != null){
            test = (Integer) incoming;
            outgoing = test;
            if (currentPlayer == firstPlayer){
                currentPlayer = secondPlayer;
            } else {
                currentPlayer = firstPlayer;
            }
            System.out.println("value is at: " + outgoing);
            currentPlayer.send(outgoing);
        }
    }
}
