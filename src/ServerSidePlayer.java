import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSidePlayer {
    private String player;
    private Socket socket;
    private ServerSidePlayer opponent;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private int score = 0;

    public ServerSidePlayer(Socket socket, String player) {
        this.socket = socket;
        this.player = player;
        try{
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Player died");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getScore() {
        return score;
    }
    public void resetScore() {
        score = 0;
    }

    public void addScorePoint() {
        score++;
    }

    public void send(Object outgoing){
        try {
            output.writeObject(outgoing);
        } catch (IOException e) {
            System.out.println();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Object receive(){
        try {
            return input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }

    public ServerSidePlayer getOpponent() {
        return opponent;
    }
}
