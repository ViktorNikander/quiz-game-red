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
    JButton b1 = new JButton();
    JButton b2 = new JButton();
    JButton b3 = new JButton();
    JButton b4 = new JButton();
    JLabel text = new JLabel();
    Object outgoing;
    Object incoming;

    Client(){
        add(base);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        try (Socket s = new Socket("127.0.0.1", 55555);
             ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(s.getInputStream())){

                System.out.println("standing by");

                incoming = ois.readObject();
                System.out.println(incoming);
                incoming = ois.readObject();
                if (incoming instanceof GamePackage){
                    GamePackage gp = (GamePackage) incoming;
                    chooseSubject(gp);
                }
                while (true){

                }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        Client c = new Client();
    }

    private void chooseSubject(GamePackage gamePackage){
        base.removeAll();
        b1.setText(gamePackage.getSubjectAlternatives().get(0).getSubject());
        base.add(b1);
        b2.setText(gamePackage.getSubjectAlternatives().get(1).getSubject());
        base.add(b2);
        b3.setText(gamePackage.getSubjectAlternatives().get(2).getSubject());
        base.add(b3);
        revalidate();
        repaint();
    }

    private void displayQuestion(Subject subject){
        base.removeAll();
        text.setText(subject.getQuestionList().get(0).getQuestion());
        base.add(text);
        b1.setText(subject.getQuestionList().get(0).getAnswer());
        base.add(b1);
        b2.setText(subject.getQuestionList().get(0).getWrongAnswersList().get(0));
        base.add(b2);
        b3.setText(subject.getQuestionList().get(0).getWrongAnswersList().get(1));
        base.add(b3);
        b4.setText(subject.getQuestionList().get(0).getWrongAnswersList().get(2));
        base.add(b4);
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("button pressed");
        JButton buttonPressed = (JButton)e.getSource();
        GamePackage gamePackage = (GamePackage) incoming;
        for (Subject subject : gamePackage.getSubjectAlternatives()){
            if (buttonPressed.getText().equalsIgnoreCase(subject.getSubject())){
                displayQuestion(subject);
            }
        }
    }
}
