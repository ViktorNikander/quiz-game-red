package QnAGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnswerButton extends JButton {
    private Boolean correct;

    public AnswerButton(String Answer, Boolean correct) {
        setText(Answer);
        setSize(300, 200);
        setFocusPainted(false);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (correct) {
                    setBackground(Color.green);
                } else if (!correct) {
                    setBackground(Color.red);
                }
            }
        });
    }
}