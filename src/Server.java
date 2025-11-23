import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread{
    private Socket s;
    private Object outgoing;
    private Object incoming;
    ServerProtocol sp = new ServerProtocol();

    public Server(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(s.getInputStream())){
            System.out.println("standing by");
            Object outgoing;
            Object incoming;

            outgoing = sp.gameLoop();
            oos.writeObject(outgoing);
            outgoing = sp.gameLoop();
            while ((incoming = ois.readObject()) != null){

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
