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
    private final Color[] buttonBackgrounds = {
            Color.WHITE,
            Color.BLUE,
            Color.BLACK,
            Color.ORANGE,
            Color.LIGHT_GRAY,
            Color.MAGENTA
    };
    private final Color[] buttonForegrounds = {
            Color.BLACK,
            Color.WHITE,
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK
    };
    private int colorIndex = 0;
    private JButton colorChangeBtn;

    public QuizGameRedClient(){
        add(base);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        add(qnAGUI,BorderLayout.SOUTH);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 25));
        add(topPanel,BorderLayout.NORTH);
        colorChangeBtn = new JButton();
        colorChangeBtn.setPreferredSize(new Dimension(20,20));
        colorChangeBtn.setBackground(buttonBackgrounds[colorIndex]);
        colorChangeBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        colorChangeBtn.setFocusPainted(false);
        colorChangeBtn.addActionListener(e -> colorChanger());
        topPanel.add(colorChangeBtn);
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
                        //TODO inform user the game is over or enable restart direct from client
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
            //TODO call a method that displays match history
            showMatchHistory();
            if (gamePackage.getIndexRoundsPlayed() >= gamePackage.getNrOfRounds()){
                gamePackage.setGameState("quit");
            }
        }
    }

    private void showMatchHistory() {
        base.removeAll();
        base.add(new JLabel("display match history"));
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

    private void colorManager(Container container){
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(buttonBackgrounds[colorIndex]);
                c.setForeground(buttonForegrounds[colorIndex]);
            } else if (c instanceof Container) {
                colorManager((Container) c);
            }
        }
    }

    private void colorChanger(){
        colorIndex = (colorIndex + 1) % buttonBackgrounds.length;

        colorChangeBtn.setBackground(buttonBackgrounds[colorIndex]);
        colorChangeBtn.setForeground(buttonForegrounds[colorIndex]);

        colorManager(this.getContentPane());

        repaint();
    }

    public static void main(String[] args) {
        QuizGameRedClient c = new QuizGameRedClient();
    }
}
