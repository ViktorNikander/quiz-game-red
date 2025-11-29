package MatchHistory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MatchHistory extends JPanel {
    private List<RoundHistory> roundHistoryList = new ArrayList<>();

    public MatchHistory(int nrOfRounds, int nrOfQuestions){
        setLayout(new GridLayout(nrOfRounds, 1));
        for (int i = 0; i < nrOfRounds; i++) {
            RoundHistory roundHistory = new RoundHistory(nrOfQuestions);
            roundHistoryList.add(roundHistory);
            add(roundHistory);
        }
    }

    public List<RoundHistory> getRoundHistoryList() {
        return roundHistoryList;
    }
}
