import MatchHistory.RoundHistory;
import QnAGUI.QnAGUI;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuizGameRedClient extends JFrame{
    private JPanel base = new JPanel();
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private GamePackage gamePackage;
    private String gameState;
    private int indexOfQuestion = 0;
    QnAGUI  qnAGUI = new QnAGUI();

    public QuizGameRedClient(){
        add(base);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        add(qnAGUI,BorderLayout.SOUTH);
        try (Socket s = new Socket("127.0.0.1", 55555)){
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());
                while ((gamePackage = (GamePackage) input.readObject()) != null){
                    gameState = gamePackage.getGameState();
                    if (gameState.equalsIgnoreCase("subject")){
                        chooseSubject();
                    } else if (gameState.equalsIgnoreCase("question")) {
                        answerQuestion(gamePackage.getChosenSubjectForRound().getQuestionList().get(indexOfQuestion));
                    } else if (gameState.equalsIgnoreCase("quit")) {
//                        int i = 1;
//                        for (RoundHistory round : gamePackage.getMatchHistory().getRoundHistoryList()){
//                            System.out.println("round " + round.getSubject() + " " + i);
//                            System.out.println(round.getAnswerHistory());
//                            i++;
//                        }
                        showMatchHistory();
                        System.out.println("quit");
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

    private void chooseSubject() {
        base.removeAll();
        for (int i = 0; i < gamePackage.getNrOfSubjects(); i++) {
            Subject subject = gamePackage.getQuestionBank().getSubjectList().get(i);
            JButton button = new JButton(subject.getSubject());
            button.addActionListener(e -> {
                gamePackage.setChosenSubjectForRound(subject);
                gamePackage.setGameState("question");
                gamePackage.getMatchHistory().getRoundHistoryList().get(gamePackage.getIndexRoundsPlayed()).setSubject(subject.getSubject());
                send(gamePackage);
            });
            base.add(button);
        }
        revalidate();
        repaint();
    }

    private void answerQuestion(Question question) {
        base.removeAll();
        base.add(new JLabel(question.getQuestion()));
        for (int i = 0; i < 4; i++) {
            String answer = question.getAllAnswers().get(i);
            if (answer.equalsIgnoreCase(question.getAnswer())){
                JButton correctButton = new JButton(answer);
                correctButton.addActionListener(e -> {
                    lockButtons();
                    correctButton.setBackground(Color.GREEN);
                    gamePackage.getMatchHistory().getRoundHistoryList()
                            .get(gamePackage.getIndexRoundsPlayed()).addAnswerHistory(true, indexOfQuestion, gamePackage.getCurrentPlayer());

                    runAfterDelay(800, () -> {
                        indexOfQuestion++;
                        checkQuestionsRemainingForRound();
                        if (gamePackage.isFirstPlayerOnSubject()){
                            runAfterDelay(800, () -> {
                                send(gamePackage);
                            });
                        } else {
                            send(gamePackage);
                        }
                    });
                });
                base.add(correctButton);
            } else {
                JButton button = new JButton(answer);
                button.addActionListener(e -> {
                    lockButtons();
                    button.setBackground(Color.RED);
                    gamePackage.getMatchHistory().getRoundHistoryList()
                            .get(gamePackage.getIndexRoundsPlayed()).addAnswerHistory(false, indexOfQuestion, gamePackage.getCurrentPlayer());

                    runAfterDelay(800, () -> {
                        indexOfQuestion++;
                        checkQuestionsRemainingForRound();
                        if (gamePackage.isFirstPlayerOnSubject()){
                            runAfterDelay(800, () -> {
                                send(gamePackage);
                            });
                        } else {
                            send(gamePackage);
                        }
                    });
                });
                base.add(button);
            }
        }
        revalidate();
        repaint();
    }

    private void lockButtons(){
        for (Component c : base.getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(false);
            }
        }
    }

    private void checkQuestionsRemainingForRound() {
        if (indexOfQuestion >= gamePackage.getNrOfQuestions()){
            indexOfQuestion = 0;
            if (gamePackage.isFirstPlayerOnSubject()){
                gamePackage.setFirstPlayerOnSubject(false);
                gamePackage.setGameState("switch");
            } else {
                gamePackage.setIndexRoundsPlayed(gamePackage.getIndexRoundsPlayed() + 1);
                gamePackage.setFirstPlayerOnSubject(true);
                gamePackage.setGameState("subject");
            }
            showMatchHistory();
            if (gamePackage.getIndexRoundsPlayed() >= gamePackage.getNrOfRounds()){
                gamePackage.setGameState("quit");
            }
        }
    }

    private void showMatchHistory() {
        base.removeAll();
        base.add(gamePackage.getMatchHistory());
        //TODO update match history for non playing player after playing player finish round.
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

    private void runAfterDelay(int delayMillisec, Runnable action) {
        javax.swing.Timer timer = new javax.swing.Timer(delayMillisec, e -> action.run());
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        QuizGameRedClient c = new QuizGameRedClient();
    }
}
