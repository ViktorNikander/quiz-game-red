package server;

import java.io.IOException;
import java.net.ServerSocket;

public class QuizGameRedServer {
    QuizGameRedServer(){
        try(ServerSocket ss = new ServerSocket(55555)){
            System.out.println("standing by for players to connect");
            while (true){
                ServerSidePlayer firstPlayer = new ServerSidePlayer(ss.accept(), "first");
                ServerSidePlayer secondPlayer = new ServerSidePlayer(ss.accept(), "second");
                ServerSideGame game = new ServerSideGame(firstPlayer, secondPlayer);
                game.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        QuizGameRedServer sl = new QuizGameRedServer();
    }
}
