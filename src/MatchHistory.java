import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MatchHistory extends JLabel {
    private List<RoundHistory> roundHistoryList = new ArrayList<>();

    public MatchHistory(Integer nrOfRounds){
        for (int i = 0; i < nrOfRounds; i++) {
            RoundHistory roundHistory = new RoundHistory();
            roundHistoryList.add(roundHistory);
        }
    }

    public List<RoundHistory> getRoundHistoryList() {
        return roundHistoryList;
    }

    public void setRoundHistoryList(List<RoundHistory> roundHistoryList) {
        this.roundHistoryList = roundHistoryList;
    }
}
