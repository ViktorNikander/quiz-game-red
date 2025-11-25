import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                    if (gamePackage.getChosenSubjectForRound() == null){
                        chooseSubject(); //TODO create method that based on action stores pressed subject as chosen subject in game package
                    }
//                    for (int i = 0; i < gamePackage.getNrOfQuestions(); i++) {
//                        answerQuestion(gamePackage.getChosenSubjectForRound().getQuestionList().get(i));
//                        //TODO create method that based on action controls if correct, stores result in game package,
//                        //TODO changes color of button, sleeps for short duration
//                    }
//                    showScoreboard(); //TODO create method that shows current score based on current score in game package
                    //TODO check that game remains in this state until opponent plays his turn and then the loop starts over
                    /*
                    Receive game package
                    Display first three subjects as buttons
                    Store pressed subject button as current subject in game package
                    Loop #nrOfQuestions
                        Display question and answers in order from chosen subject as buttons
                        Compare pressed button to correct answer and store true/false for round
                        Change correct button to green
                        If wrong button pressed, change it to red
                        Sleep for short duration
                    Update scoreboard and show it
                    Window will hopefully "sleep" in this state until new round begins based on opponents action
                     */
                }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private void answerQuestion(Question question) {
//        base.removeAll();
//        base.add(new JLabel(question.getQuestion()));
//        for (int i = 0; i < 4; i++) {
//            String answer = question.getAllAnswers().get(i);
//            if (answer.equalsIgnoreCase(question.getAnswer())){
//                JButton correctButton = new JButton(answer);
//                correctButton.addActionListener(e -> {
//
//                });
//            }
//            JButton button = new JButton(answer);
//            button.addActionListener(e -> {
//                if (!correctAnswer(answer)){
//                    button.setBackground(Color.RED);
//                }
//            });
//            base.add(button);
//        }
//        revalidate();
//        repaint();
//    }

    private void chooseSubject() {
        base.removeAll();
        for (int i = 0; i < gamePackage.getNrOfSubjects(); i++) {
            Subject subject = gamePackage.getQuestionBank().getSubjectList().get(i);
            JButton button = new JButton(subject.getSubject());
            button.addActionListener(e -> {
                gamePackage.setChosenSubjectForRound(subject);
            });
            base.add(button);
        }
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        QuizGameRedClient c = new QuizGameRedClient();
    }
}
