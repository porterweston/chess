package reqres;
import chess.*;

public record JoinGameRequest(String authToken, ChessGame.TeamColor playerColor, Integer gameID) {
}
