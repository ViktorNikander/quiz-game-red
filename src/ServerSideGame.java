public class ServerSideGame {
    private ServerSidePlayer firstPlayer;
    private ServerSidePlayer secondPlayer;
    private ServerSidePlayer currentPlayer;

    public ServerSideGame(ServerSidePlayer firstPlayer, ServerSidePlayer secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.currentPlayer = firstPlayer;
        this.firstPlayer.setOpponent(secondPlayer);
        this.secondPlayer.setOpponent(firstPlayer);
    }
}
