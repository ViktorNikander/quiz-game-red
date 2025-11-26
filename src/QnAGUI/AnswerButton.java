package QnAGUI;

import javax.swing.*;

public class AnswerButton extends JButton {
    Boolean correct;

    public AnswerButton(String Answer, Boolean correct) {
        setText(Answer);
        setSize(300, 200);
        setFocusPainted(false);
    }
}
