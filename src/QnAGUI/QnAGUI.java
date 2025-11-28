package QnAGUI;

import javax.swing.*;
import java.awt.*;

public class QnAGUI extends JPanel {
    AnswerButton a1 = new AnswerButton("Answer1", true);
    AnswerButton a2 = new AnswerButton("Answer2", false);
    AnswerButton a3 = new AnswerButton("Answer3", false);
    AnswerButton a4 = new AnswerButton("Answer4", false);
    Question q = new Question("Question1 (Answer is 1)");

    public QnAGUI() {
        setSize(600,800);
        setLayout(new BorderLayout());
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2,2));
        answerPanel.add(a1);
        answerPanel.add(a2);
        answerPanel.add(a3);
        answerPanel.add(a4);
        add(answerPanel,BorderLayout.CENTER);
        JLabel question = q;
        add(question,BorderLayout.NORTH);
    }
}
