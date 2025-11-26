import QnAGUI.QnAGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuizGameRedClient extends JFrame implements ActionListener {
    JPanel base = new JPanel();
    JLabel currentValue = new JLabel();
    JButton button = new JButton();
    ObjectOutputStream output;
    ObjectInputStream input;
    Object outgoing;
    Object incoming;
    QnAGUI  qnAGUI = new QnAGUI();

    public QuizGameRedClient(){
        add(base);
        button.addActionListener(this);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        add(qnAGUI,BorderLayout.SOUTH);
        try (Socket s = new Socket("127.0.0.1", 55555)){
                output = new ObjectOutputStream(s.getOutputStream());
                input = new ObjectInputStream(s.getInputStream());
                System.out.println("standing by for game to start");
                while ((incoming = input.readObject()) != null){
                    System.out.println("received value");
                    drawButton(incoming);
                    System.out.println("method called");
                }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawButton(Object incoming) {
        currentValue.setText("Current value: " + incoming);
        System.out.println(currentValue.getText());
        base.add(currentValue);
        base.add(button);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        QuizGameRedClient c = new QuizGameRedClient();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("button pressed");
        sendUpdate();
    }

    private void sendUpdate() {
        outgoing = (Integer) incoming + 1;
        try {
            output.writeObject(outgoing);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
