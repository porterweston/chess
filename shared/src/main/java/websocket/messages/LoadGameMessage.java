package websocket.messages;

import chess.*;

public class LoadGameMessage extends ServerMessage{
    private final ChessGame game;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return this.game;
    }
}
