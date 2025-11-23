import java.util.Collections;

public class ServerProtocol {
    private final int INITIAL = 0;
    private final int SUBJECT = 1;
    private final int QUESTION = 2;

    private int STATE = INITIAL;

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
