package match.history;

import javax.swing.*;

public class test extends JFrame {
    MatchHistory matchHistory = new MatchHistory(6, 3);
    public test(){
        add(matchHistory);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        test test = new test();
    }
}
