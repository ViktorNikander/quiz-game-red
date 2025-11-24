import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class ServerProtocol {
    private final int INITIAL = 0;
    private final int SUBJECT = 1;
    private final int QUESTION = 2;
    private int amountOfQuestions;
    private int amountOfRounds;

    private int STATE = INITIAL;

    public ServerProtocol(){
        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/GameSettings.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        amountOfQuestions = Integer.parseInt(p.getProperty("questionsPerRound", "2"));
        amountOfRounds = Integer.parseInt(p.getProperty("roundsPerGame", "2"));
        //TODO implement values in protocol
    }

    QuestionBank qb = new QuestionBank();

    public Object gameLoop(){
        if (STATE == INITIAL){
            STATE = SUBJECT;
            return "CONNECTED";
        } else if (STATE == SUBJECT) {
            STATE = QUESTION;
            Collections.shuffle(qb.getSubjectList());
            GamePackage gp = new GamePackage();
            for (int i = 0; i < 3; i++) {
                gp.getSubjectAlternatives().add(qb.getSubjectList().get(i));
            }
            return gp;
        } else if (STATE == QUESTION) {
            STATE = SUBJECT;
            return null;
        } else {
            return null;
        }
    }
}
