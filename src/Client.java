import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends JFrame implements ActionListener {
    JPanel base = new JPanel(new GridLayout(3,1));
    JPanel question = new JPanel();
    JPanel answerAlternatives = new JPanel();
    JPanel subjectAlternatives = new JPanel();
    JPanel top = new JPanel();
    JPanel bottom = new JPanel();
    JButton blueTop = new JButton("Change to blue at top");
    JButton redTop = new JButton("Change to blue at bottom");
    JButton blueBottom = new JButton("Change to red at top");
    JButton redBottom = new JButton("Change to red at bottom");
    JButton blackTop = new JButton("Change to black at top");
    JButton blackBottom = new JButton("Change to black at bottom");

    Client(){
        add(base);
        question.setBackground(Color.BLUE);
        answerAlternatives.setBackground(Color.RED);
        subjectAlternatives.setBackground(Color.black);

        base.add(top);
        base.add(bottom);

        blueTop.addActionListener(this);
        blueBottom.addActionListener(this);
        redTop.addActionListener(this);
        redBottom.addActionListener(this);
        blackTop.addActionListener(this);
        blackBottom.addActionListener(this);
        top.add(blueTop);
        top.add(blueBottom);
        top.add(redTop);
        top.add(redBottom);
        top.add(blackTop);
        top.add(blackBottom);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        try (Socket s = new Socket("127.0.0.1", 55555);
             ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(s.getInputStream())){

                System.out.println("standing by");
                while (true);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void changeToBlue(){
        bottom.removeAll();
        bottom.add(question);
        validate();
        repaint();
    }
    private void changeToRed(){
        bottom.removeAll();
        bottom.add(answerAlternatives);
        validate();
        repaint();
    }
    private void changeToBlack(){
        bottom.removeAll();
        bottom.add(subjectAlternatives);
        validate();
        repaint();
    }
    public static void main(String[] args) {
        Client c = new Client();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("button pressed");
        if (e.getSource() == blueTop || e.getSource() == blueBottom){
            changeToBlue();
        } else if (e.getSource() == redBottom || e.getSource() == redTop) {
            changeToRed();
        } else if (e.getSource() == blackBottom || e.getSource() == blackTop) {
            changeToBlack();
        }
    }
}
