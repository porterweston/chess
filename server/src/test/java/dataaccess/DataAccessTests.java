package dataaccess;

import dataaccess.mysql.*;
import model.*;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    /*
        USER TESTS
     */
    @Test
    @Order(1)
    public void createUserPositive() {
        clearUsers();
        try {
            userDAO.createUser(new UserData("johndoe", "12345", "johndoe@email.com"));
        } catch (DataAccessException e) {
            System.out.printf("Unable to create user: %s", e.getMessage());
        }
        getUserPositive();
    }

    @Test
    @Order(2)
    public void createUserNegative() {
        //try to create already existing user
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData("johndoe", "12345", "johndoe@email.com")));
    }

    @Test
    @Order(3)
    public void getUserPositive() {
        UserData actualUser = userDAO.getUser("johndoe");
        UserData expectedUser = new UserData("johndoe", "12345", "johndoe@email.com");
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    @Order(4)
    public void getUserNegative() {
        //try to get a non-existing user
        UserData actualUser = userDAO.getUser("doejohn");
        Assertions.assertNull(actualUser);
    }

    @Test
    @Order(5)
    public void clearUsers() {
        userDAO.deleteUsers();
        UserData actualUser = userDAO.getUser("johndoe");
        Assertions.assertNull(actualUser);
    }
}