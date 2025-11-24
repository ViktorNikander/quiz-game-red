import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    ServerListener(){
        try(ServerSocket ss = new ServerSocket(55555)){
            System.out.println("standing by");
            while (true){
                Socket s = ss.accept();
                Server server = new Server(s);
                server.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        ServerListener sl = new ServerListener();
    }
}
