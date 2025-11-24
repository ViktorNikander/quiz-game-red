import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSidePlayer {
    private String player;
    private Socket socket;
    private ServerSidePlayer opponent;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ServerSidePlayer(Socket socket, String player) {
        this.socket = socket;
        this.player = player;


    }

    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }

    public ServerSidePlayer getOpponent() {
        return opponent;
    }
}
