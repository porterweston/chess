import chess.*;
import server.Server;
import service.*;
import dataaccess.interfaces.*;
import dataaccess.mysql.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        try {
            GameService gameService = new GameService(new MySQLGameDAO(), new MySQLAuthDAO());
            UserService userService = new UserService(new MySQLAuthDAO(), new MySQLUserDAO());
            ClearService clearService = new ClearService(new MySQLUserDAO(), new MySQLGameDAO(), new MySQLAuthDAO());
            Server server = new Server(gameService, userService, clearService);
            server.run(8080);
        } catch (Throwable e) {
            System.out.printf("Unable to start server: %s%n", e.getMessage());
        }


        //chessServer.stop();
    }
}