package dataAccess;

import model.GameData;
import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    //Singleton pattern
    private static MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryGameDAO(){}
    public static MemoryGameDAO getInstance() {
        return gameDAO;
    }

    //data
    private ArrayList<GameData> games = new ArrayList<GameData>();

    //methods
    @Override
    public GameData getGame(int gameID){
        return null;
    }
    @Override
    public GameData[] listGames(){
        return null;
    }
    @Override
    public void createGame(String gameName){

    }
    @Override
    public boolean joinGame(String clientColor, int gameID){
        return false;
    }
    @Override
    public void clearGames(){
        this.games.clear();
    }

    public ArrayList<GameData> getAllGames(){
        return this.games;
    }
}
