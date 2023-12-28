package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserMapperTest {

    /*

    private static final String USER = "notactive";
    private static final String PASSWORD = "notactive";
    private static final String URL = "jdbc:postgresql://localhost:5432/notactive";
    private static final String DB = "notactive";
    private static String API_KEY = System.getenv("SENDGRID_API_KEY");
    User user;

    User user1;
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    @BeforeEach
    void setUp() {
        user = new User("TestName", "test@testing.dk", "TestPassword", "TestAdress", 23232323, 4000, true, 1);
    }

    @Test
    void addUser() {
        try {
            user1 = UserMapper.addUser(user, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        assertEquals(10, user1.getId());
        
    }

     */
}