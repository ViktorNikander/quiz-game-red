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
import java.net.InetAddress;
import java.net.MulticastSocket;

public class QuizGameRedClient extends JFrame{
    private JPanel base = new JPanel();
    private JPanel sidePanel = new JPanel();
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private GamePackage gamePackage;
    private String gameState;
    private int indexOfQuestion = 0;
    private JLabel playerLabel = new JLabel("Connecting");
    private JLabel scoreLabel = new JLabel("Score: 0");
    private boolean identitySet = false;
    private String player = null;
    private final ImageIcon[] avatars = {
            null,
            new ImageIcon("avatar1.png"),
            new ImageIcon("avatar2.png"),
            new ImageIcon("avatar3.png"),
            new ImageIcon("avatar4.png"),
            new ImageIcon("avatar5.png")
    };
    private int avatarIndex = 0;
    private JButton avatarBtn;
    private JLabel avatarLabel;
    private final Color[] buttonBackgrounds = {
            Color.WHITE,
            Color.BLUE,
            Color.BLACK,
            Color.ORANGE,
            Color.LIGHT_GRAY,
            Color.MAGENTA,
            Color.YELLOW
    };
    private final Color[] buttonForegrounds = {
            Color.BLACK,
            Color.WHITE,
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.WHITE
    };
    private int colorIndex = 0;
    private JButton colorChangeBtn;
    private String playerName;

    public QuizGameRedClient(){
        playerName = JOptionPane.showInputDialog("What is your name?");
        if (playerName == null || playerName.trim().isEmpty()){
            playerName = "Player";
        }

        setSize(940, 620);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(500, 620));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(500, 20));
        topPanel.add(playerLabel, BorderLayout.WEST);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        base.setPreferredSize(new Dimension(500, 600));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(base, BorderLayout.CENTER);

        sidePanel.setPreferredSize(new Dimension(400, 620));

        JPanel colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(400, 120));
        colorChangeBtn = new JButton("Change Color");
        colorChangeBtn.setPreferredSize(new Dimension(200,100));
        setSize(500, 600);
        JPanel topCPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 25)); //Ändra plats på knappen
        add(topCPanel,BorderLayout.NORTH);
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(50, 50));
        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarBtn = new JButton("Avatar");
        avatarBtn.setFocusPainted(false);
        avatarBtn.addActionListener(e -> changeAvatar());
        colorChangeBtn = new JButton();
        colorChangeBtn.setPreferredSize(new Dimension(20,20));
        colorChangeBtn.setBackground(buttonBackgrounds[colorIndex]);
        colorChangeBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        colorChangeBtn.setFocusPainted(false);
        colorChangeBtn.addActionListener(e -> colorChanger());
        colorPanel.add(colorChangeBtn);

        sidePanel.add(colorPanel,BorderLayout.NORTH);
        startChat();
        //
        add(mainPanel,BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        topCPanel.add(colorChangeBtn);
        topCPanel.add(avatarBtn);
        topCPanel.add(avatarLabel);
        try (Socket s = new Socket("127.0.0.1", 55555)){
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());
                while ((gamePackage = (GamePackage) input.readObject()) != null){
                    if (!identitySet){
                        String current = gamePackage.getCurrentPlayer();
                        player = current;
                        if ("first".equalsIgnoreCase(current)) {
                            playerLabel.setText("Player 1");
                        } else if ("second".equalsIgnoreCase(current)) {
                            playerLabel.setText("Player 2");
                        }
                        identitySet = true;
                    }
                    if (player != null){
                        int myScore = 0;
                        if ("first".equalsIgnoreCase(player)){
                            myScore = gamePackage.getFirstPlayerScore();
                        } else if ("second".equalsIgnoreCase(player)){
                            myScore = gamePackage.getSecondPlayerScore();
                        }
                        scoreLabel.setText("Score: " + myScore);
                    }
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

    private void startChat(){
        try{
            InetAddress ip = InetAddress.getByName("230.0.0.0");
            MulticastSocket socket = new MulticastSocket(4444);
            socket.joinGroup(ip);
            Chat chatPanel = new Chat(playerName, ip,4444, socket);
            chatPanel.setPreferredSize(new Dimension(380, 400));
            sidePanel.add(chatPanel, BorderLayout.CENTER);
            sidePanel.revalidate();
            sidePanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void changeAvatar() {
        avatarIndex = (avatarIndex + 1) % avatars.length;

        avatarLabel.setIcon(avatars[avatarIndex]);

        repaint();
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
        colorManager(base);
        revalidate();
        repaint();
    }

    private void answerQuestion(Question question) {
        base.removeAll();

        JPanel qnaPanel = new JPanel(new BorderLayout());
        JPanel qAlignmentPanel = new JPanel(new BorderLayout());
        JPanel qAlignmentPushPanel = new JPanel();
        JTextPane questionPane = new JTextPane();
        JPanel answerPanel = new JPanel(new GridLayout(2, 2));

        qnaPanel.setPreferredSize(new Dimension(400, 500));
        qAlignmentPanel.setPreferredSize(new Dimension(400, 200));
        qAlignmentPushPanel.setPreferredSize(new Dimension(400, 80));
        questionPane.setPreferredSize(new Dimension(400, 120));
        answerPanel.setPreferredSize(new Dimension(400, 300));

        StyledDocument doc = questionPane.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), set, false);
        questionPane.setText(question.getQuestion());
        questionPane.setEditable(false);
        questionPane.setFocusable(false);
        questionPane.setOpaque(false);

        qAlignmentPanel.add(questionPane, BorderLayout.SOUTH);
        qAlignmentPanel.add(qAlignmentPushPanel, BorderLayout.NORTH);
        qnaPanel.add(qAlignmentPanel,BorderLayout.NORTH);
        qnaPanel.add(answerPanel,BorderLayout.CENTER);
        base.add(qnaPanel,BorderLayout.CENTER);

        for (int i = 0; i < 4; i++) {
            String answer = question.getAllAnswers().get(i);
            if (answer.equalsIgnoreCase(question.getAnswer())){
                JButton correctButton = new JButton(answer);
                correctButton.setFocusPainted(false);
                correctButton.addActionListener(e -> {
                    gamePackage.setLastAnswerCorrect(1);
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
                    gamePackage.setLastAnswerCorrect(0);
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

        colorManager(base);
        revalidate();
        repaint();
    }

    private void runAfterDelay(int delayMillisec, Runnable action) {
        javax.swing.Timer timer = new javax.swing.Timer(delayMillisec, e -> action.run());
        timer.setRepeats(false);
        timer.start();
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

        JPanel matchHistoryPanel = new JPanel(new BorderLayout());
        matchHistoryPanel.add(gamePackage.getMatchHistory(), BorderLayout.CENTER);
        gamePackage.getMatchHistory().setPreferredSize(new Dimension(400,450));
        base.add(matchHistoryPanel,BorderLayout.CENTER);
        String msg = gamePackage.getFinalResultMessage();

        if (msg != null && !msg.isEmpty() && gamePackage.getGameState() != null && gamePackage.getGameState().equalsIgnoreCase("quit")) {
            JPanel resultPanel = new JPanel(new BorderLayout());
            JLabel titleLabel = new JLabel("Game result:");
            titleLabel.setHorizontalAlignment(JLabel.CENTER);

            JTextPane resultPane = new JTextPane();
            resultPane.setPreferredSize(new Dimension(400,150));
            StyledDocument doc = resultPane.getStyledDocument();
            SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), set, false);
            resultPane.setText(msg);
            resultPane.setEditable(false);
            resultPane.setFocusable(false);
            resultPane.setOpaque(false);
            resultPanel.add(titleLabel, BorderLayout.NORTH);
            resultPanel.add(resultPane, BorderLayout.CENTER);
            matchHistoryPanel.add(resultPanel, BorderLayout.SOUTH);
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
