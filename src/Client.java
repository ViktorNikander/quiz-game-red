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

                Object outgoing;
                Object incoming;

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

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
