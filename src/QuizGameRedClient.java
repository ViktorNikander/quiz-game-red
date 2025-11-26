import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuizGameRedClient extends JFrame {
    JPanel base = new JPanel();
    ObjectOutputStream output;
    ObjectInputStream input;
    GamePackage gamePackage;
    String gameState;

    public QuizGameRedClient(){
        add(base);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        try (Socket s = new Socket("127.0.0.1", 55555)){
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());
                while ((gamePackage = (GamePackage) input.readObject()) != null){
                    System.out.println("received from server");
                    gameState = gamePackage.getGameState();
                    if (gameState.equalsIgnoreCase("subject")){
                        chooseSubject();
                    } else if (gameState.equalsIgnoreCase("question")) {
                    } else if (gameState.equalsIgnoreCase("switch")) {
                    } else if (gameState.equalsIgnoreCase("quit")) {
                        //TODO quit logic
                    }
                }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void answerQuestion(Question question) {
        base.removeAll();
        base.add(new JLabel(question.getQuestion()));
        for (int i = 0; i < 4; i++) {
            String answer = question.getAllAnswers().get(i);
            if (answer.equalsIgnoreCase(question.getAnswer())){
                JButton correctButton = new JButton(answer);
                correctButton.addActionListener(e -> {
                    //TODO add action for pressing correct button
                    setPressedSubject(correctButton.getText());
                });
                base.add(correctButton);
            } else {
                JButton button = new JButton(answer);
                button.addActionListener(e -> {
                    setPressedSubject(button.getText());
                    //TODO add action for pressing wrong button
                });
                base.add(button);
            }
        }
        revalidate();
        repaint();
    }

    private void setPressedSubject(String pressedSubject) {
        for (Subject subject:gamePackage.getQuestionBank().getSubjectList()) {
            if (subject.getSubject().equalsIgnoreCase(pressedSubject)) {
                gamePackage.setChosenSubjectForRound(subject);
            }
        }
    }

    private void chooseSubject() {
        System.out.println("inside chooseSubject()");
        base.removeAll();
        for (int i = 0; i < gamePackage.getNrOfSubjects(); i++) {
            Subject subject = gamePackage.getQuestionBank().getSubjectList().get(i);
            JButton button = new JButton(subject.getSubject());
            button.addActionListener(e -> {
                gamePackage.setChosenSubjectForRound(subject);
                System.out.println(subject.getSubject() + " chosen");
                System.out.println("sending to server");
                send(gamePackage);
            });
            base.add(button);
        }
        revalidate();
        repaint();
    }

    private void send(GamePackage gamePackage) {
        try {
            output.writeObject(gamePackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        QuizGameRedClient c = new QuizGameRedClient();
    }
}
