package MatchHistory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoundHistory extends JPanel {
    private int nrOfQuestions;
    private String subject;
    private List<Boolean> answerHistory = new ArrayList<>();
    private List<JPanel> panelList = new ArrayList<>();

    public RoundHistory(int nrOfQuestions){
        this.nrOfQuestions = nrOfQuestions;
        JPanel panel;
        for (int i = 0; i < nrOfQuestions * 2 + 1; i++) { // * 2 makes enough labels for both players in round and + 1 makes label for Subject
            panel = new JPanel();
            if (i == nrOfQuestions){
                panel.add(new JLabel(subject));
            } else {
                panel.setBackground(Color.LIGHT_GRAY);
            }
            add(panel);
            panelList.add(panel);
        }
    }


    public void setSubject(String subject) {
        this.subject = subject;
        setSubjectGUI();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubjectGUI() {
        this.panelList.get(nrOfQuestions).add(new JLabel(subject));
    }

    public List<Boolean> getAnswerHistory() {
        return answerHistory;
    }

    public void addAnswerHistory(boolean isCorrect, int indexOfQuestion, String currentPlayer){
        if (currentPlayer.equalsIgnoreCase("second")){
            indexOfQuestion= indexOfQuestion + nrOfQuestions + 1;
        }
        System.out.println(indexOfQuestion);
        if (isCorrect){
            panelList.get(indexOfQuestion).setBackground(Color.GREEN);
        } else {
            panelList.get(indexOfQuestion).setBackground(Color.RED);
        }
    }
}
