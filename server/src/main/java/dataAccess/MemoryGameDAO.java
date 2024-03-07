package dataAccess;

import model.GameData;

public class MemoryGameDAO implements GameDAO{
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

    }
}
