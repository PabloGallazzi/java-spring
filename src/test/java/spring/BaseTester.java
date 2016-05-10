package spring;

import domain.*;
import domain.Character;
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
import repositories.AuthRepository;
import repositories.CharactersRepository;
import repositories.TeamsRepository;
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

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private TeamsRepository teamsRepository;

    @Autowired
    private CharactersRepository charactersRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    private User getTACSTestUserVO() {
        String id = "123456789012345678901234";
        User user = new User("TACS", "testPass123;");
        User.validateUser(user);
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        return user;
    }

    private User getTACSAdminTestUserVO() {
        User user = getTACSTestUserVO();
        user.setIsAdmin(true);
        return user;
    }

    public Thumbnail getTACSTestThumbnailVO() {
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setPath("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784");
        thumbnail.setExtension("JPG");
        return thumbnail;
    }

    public Character getTACSTestCharacterVO() {
        Character character = new Character();
        Thumbnail thumbnail = getTACSTestThumbnailVO();
        character.setThumbnail(thumbnail);
        character.setId(1011334);
        character.setName("3-D Man");
        return character;
    }

    private Team getTACSTestTeamVO() {
        Team team = new Team();
        team.setTeamName("uno");
        return team;
    }

    public Team getTACSTestTeamWithMemberVO() {
        Team team = getTACSTestTeamVO();
        team.setTeamName("uno");
        team.addMember(createTACSTestCharacter());
        return team;
    }

    public User createTACSTestUser() {
        User user = getTACSTestUserVO();
        ds.getDatastore().save(user);
        return user;
    }

    public User createTACSAdminTestUser() {
        User user = getTACSAdminTestUserVO();
        ds.getDatastore().save(user);
        return user;
    }

    public Token createAndLogInTACSTestUser() {
        User user = createTACSTestUser();
        user.setUserPassword("testPass123;");
        return authRepository.login(user);
    }

    public Token createAndLogInTACSAdminTestUser() {
        User user = createTACSAdminTestUser();
        user.setUserPassword("testPass123;");
        return authRepository.login(user);
    }

    public Team createTACSTestTeamWithMember() {
        return teamsRepository.save(getTACSTestTeamWithMemberVO());
    }

    public Character createTACSTestCharacter() {
        return charactersRepository.save(getTACSTestCharacterVO());
    }

    public void deleteTACSTestUser() {
        ds.getDatastore().delete(getTACSTestUser());
    }

    public void deleteTACSTestUserToken() {
        Token token = getTACSTestUserToken();
        ds.getDatastore().delete(token);
    }

    public void deleteTACSTestUserWithToken() {
        deleteTACSTestUserToken();
        ds.getDatastore().delete(getTACSTestUser());
    }

    public void deleteTACSTestCharacter() {
        Character character = getTACSTestCharacter();
        Thumbnail thumbnail = getTACSTestThumbnail();
        ds.getDatastore().delete(thumbnail);
        ds.getDatastore().delete(character);
    }

    public void deleteTACSTestTeamWithMember() {
        deleteTACSTestCharacter();
        Team team = ds.getDatastore().find(Team.class, "teamName", "uno").get();
        ds.getDatastore().delete(team);
    }

    public Token getTACSTestUserToken() {
        return ds.getDatastore().find(Token.class, "userId", getTACSTestUser().getUserId()).get();
    }

    public User getTACSTestUser() {
        return ds.getDatastore().find(User.class, "userName", "TACS").get();
    }

    public Character getTACSTestCharacter() {
        return ds.getDatastore().find(Character.class, "id", 1011334).get();
    }

    public Thumbnail getTACSTestThumbnail() {
        return ds.getDatastore().find(Thumbnail.class, "path", "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784").get();
    }

}