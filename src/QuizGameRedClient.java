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
    GamePackage gamePackage;

    public QuizGameRedClient(){
        add(base);
        button.addActionListener(this);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        try (Socket s = new Socket("127.0.0.1", 55555)){
                while ((gamePackage = (GamePackage) input.readObject()) != null){
                    /*
                    Receive game package
                    Display first three subjects as buttons
                    Store pressed subject button as current subject in game package
                    Loop #nrOfQuestions
                        Display question and answers in order from chosen subject as buttons
                        Compare pressed button to correct answer and store true/false for round
                        Change correct button to green
                        If wrong button pressed, change it to red
                        Sleep for short duration
                    Update scoreboard and show it
                    Window will hopefully "sleep" in this state until new round begins based on opponents action
                     */
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
