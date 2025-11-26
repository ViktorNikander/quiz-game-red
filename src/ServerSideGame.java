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
    GamePackage gamePackage = new GamePackage(new QuestionBank());

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
        gamePackage.setNrOfQuestions(nrOfQuestions);
        gamePackage.setNrOfSubjects(nrOfSubjects);
        gamePackage.setNrOfRounds(nrOfRounds);
    }

    @Override
    public void run() {
        String gameState = "subject";
        gamePackage.setGameState(gameState);
        gamePackage.shuffleAll();
        currentPlayer.send(gamePackage);
        while ((gamePackage = (GamePackage) currentPlayer.receive()) != null){
            gameState = gamePackage.getGameState();
            if (gameState.equalsIgnoreCase("subject")){
                gamePackage.shuffleAll();
                gamePackage.setGameState("subject");
                currentPlayer.send(gamePackage);
            } else if (gameState.equalsIgnoreCase("question")) {
                currentPlayer.send(gamePackage);
            } else if (gameState.equalsIgnoreCase("switch")) {
                changeCurrentPlayer();
                gamePackage.setGameState("question");
                currentPlayer.send(gamePackage);
            } else if (gameState.equalsIgnoreCase("quit")) {
                //TODO quit logic
                firstPlayer.send(gamePackage);
                secondPlayer.send(gamePackage);
                System.out.println("quit");
            }
        }
    }

    private void changeCurrentPlayer() {
        if (currentPlayer == firstPlayer){
            currentPlayer = secondPlayer;
        }else {
            currentPlayer = firstPlayer;
        }
    }
}
