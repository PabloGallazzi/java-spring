package spring;

import domain.Token;
import domain.User;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import services.DSMongoInterface;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by pgallazzi on 3/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Ignore
public class BaseTester {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private DSMongoInterface ds;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    public User createTACSTestUser() {
        String id = "123456789012345678901234";
        User user = new User("TACS", "testPass123;");
        User.validateUser(user);
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        ds.getDatastore().save(user);
        return user;
    }

    public User createTACSAdminTestUser() {
        String id = "123456789012345678901234";
        User user = new User("TACS", "testPass123;");
        User.validateUser(user);
        user.setIsAdmin(true);
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        ds.getDatastore().save(user);
        return user;
    }

    public void deleteTACSTestUser() {
        ds.getDatastore().delete(getTACSTestUser());
    }

    public void deleteTACSTestUserWithToken() {
        deleteTACSTestUserToken();
        ds.getDatastore().delete(getTACSTestUser());
    }

    public Token getTACSTestUserToken() {
        return ds.getDatastore().find(Token.class, "userId", getTACSTestUser().getUserId()).get();
    }

    public User getTACSTestUser() {
        return ds.getDatastore().find(User.class, "userName", "TACS").get();
    }

    public void deleteTACSTestUserToken() {
        Token token = getTACSTestUserToken();
        ds.getDatastore().delete(token);
    }

}
