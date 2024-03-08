package dataAccess;

import java.util.ArrayList;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{
    //Singleton pattern
    private static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private MemoryAuthDAO(){}
    public static MemoryAuthDAO getInstance() {
        return authDAO;
    }

    //data
    private ArrayList<AuthData> auths = new ArrayList<AuthData>();

    //methods
    @Override
    public AuthData getAuth(String authToken){
        for (AuthData auth : this.auths){
            if (auth.authToken().equals(authToken)){
                return auth;
            }
        }
        return null;
    }
    @Override
    public void createAuth(String username){

    }
    @Override
    public void deleteAuth(String authToken){
        this.auths.removeIf(auth -> auth.authToken().equals(authToken));
    }
    @Override
    public void clearAuths(){
        this.auths.clear();
    }
}
