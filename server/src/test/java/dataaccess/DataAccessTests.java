package dataaccess;

import dataaccess.mysql.*;
import model.*;

import org.junit.jupiter.api.*;

public class DataAccessTests {
    //data access instances
    private static MySQLUserDAO userDAO;
    private static MySQLGameDAO gameDAO;
    private static MySQLAuthDAO authDAO;

    @BeforeAll
    public static void init() {
        try {
            userDAO = new MySQLUserDAO();
            gameDAO = new MySQLGameDAO();
            authDAO = new MySQLAuthDAO();
        }
        catch (DataAccessException e) {
            System.out.printf("Unable to initialize database: %s", e.getMessage());
        }
    }

    @Test
    public void getUserPositive() {
        return;
    }

    @Test
    public void createUserPositive() {
        try {
            userDAO.createUser(new UserData("johndoe", "12345", "johndoe@email.com"));
        } catch (DataAccessException e) {
            System.out.printf("Unable to create user: %s", e.getMessage());
        }
    }
}
