package client;

import questions.Question;
import questions.Subject;
import server.GamePackage;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
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

    public QuizGameRedClient(){
        add(base);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 600);
        try (Socket s = new Socket("127.0.0.1", 55555)){
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());
                while ((gamePackage = (GamePackage) input.readObject()) != null){
                    gameState = gamePackage.getGameState();
                    if (gameState.equalsIgnoreCase("subject")){
                        chooseSubject();
                    } else if (gameState.equalsIgnoreCase("question")) {
                        answerQuestion(gamePackage.getChosenSubjectForRound().getQuestionList().get(indexOfQuestion));
                    } else if (gameState.equalsIgnoreCase("get update")) {
                        showMatchHistory();
                        gamePackage.setGameState("subject");
                        send(gamePackage);
                    } else if (gameState.equalsIgnoreCase("quit")) {
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
        JPanel subjectMainPanel = new JPanel(new BorderLayout());
        JLabel subjectLabel = new JLabel("Choose Subject");
        JPanel subjectPanel = new JPanel(new GridLayout(3, 1));

        subjectMainPanel.setPreferredSize(new Dimension(400, 500));
        subjectLabel.setPreferredSize(new Dimension(400, 120));
        subjectPanel.setPreferredSize(new Dimension(400, 480));

        subjectLabel.setHorizontalAlignment(JLabel.CENTER);
        subjectLabel.setVerticalAlignment(JLabel.CENTER);

        subjectMainPanel.add(subjectLabel, BorderLayout.NORTH);
        subjectMainPanel.add(subjectPanel, BorderLayout.CENTER);
        base.add(subjectMainPanel, BorderLayout.CENTER);

        for (int i = 0; i < gamePackage.getNrOfSubjects(); i++) {
            Subject subject = gamePackage.getQuestionBank().getSubjectList().get(i);
            JButton button = new JButton(subject.getSubject());
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                gamePackage.setChosenSubjectForRound(subject);
                gamePackage.setGameState("question");
                gamePackage.getMatchHistory().getRoundHistoryList().get(gamePackage.getIndexRoundsPlayed()).setSubject(subject.getSubject());
                send(gamePackage);
            });
            subjectPanel.add(button);
        }
        revalidate();
        repaint();
    }

    private void answerQuestion(Question question) {
        base.removeAll();

        JPanel qnaPanel = new JPanel(new BorderLayout());
        JTextPane questionArea = new JTextPane();
        JPanel answerPanel = new JPanel(new GridLayout(2, 2));

        qnaPanel.setPreferredSize(new Dimension(400, 500));
        questionArea.setPreferredSize(new Dimension(400, 200));
        answerPanel.setPreferredSize(new Dimension(400, 300));

        StyledDocument doc = questionArea.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), set, false);
        questionArea.setText(question.getQuestion());
        questionArea.setEditable(false);
        questionArea.setFocusable(false);
        questionArea.setOpaque(false);

        qnaPanel.add(questionArea,BorderLayout.NORTH);
        base.add(qnaPanel,BorderLayout.CENTER);

        for (int i = 0; i < 4; i++) {
            String answer = question.getAllAnswers().get(i);
            if (answer.equalsIgnoreCase(question.getAnswer())){
                JButton correctButton = new JButton(answer);
                correctButton.setFocusPainted(false);
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
                answerPanel.add(correctButton);
            } else {
                JButton button = new JButton(answer);
                button.setFocusPainted(false);
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
                answerPanel.add(button);
            }
        }
        qnaPanel.add(answerPanel,BorderLayout.CENTER);
        base.add(qnaPanel,BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void lockButtons(){
        for (Component c : base.getComponents()) {
            if (c instanceof JPanel) {
                for (Component b : ((JPanel) c).getComponents()) {
                    if (b instanceof JPanel) {
                        for (Component bs : ((JPanel) b).getComponents()) {
                            if (bs instanceof JButton) {
                                bs.setEnabled(false);
                            }
                        }
                    }
                }
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
                gamePackage.setGameState("send update");
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
