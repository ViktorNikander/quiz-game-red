import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread{
    private Socket s;
    private Object outgoing;
    private Object incoming;

    public Server(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(s.getInputStream())){
            System.out.println("standing by");
            while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
