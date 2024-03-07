package dataAccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{
    @Override
    public AuthData getAuth(String authToken){
        return null;
    }
    @Override
    public void createAuth(String username){

    }
    @Override
    public void deleteAuth(String authToken){

    }
    @Override
    public void clearAuths(){

    }
}
