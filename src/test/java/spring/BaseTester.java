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

import java.util.Date;

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
    protected DSMongoInterface ds;

    @Autowired
    protected AuthRepository authRepository;

    @Autowired
    protected TeamsRepository teamsRepository;

    @Autowired
    protected CharactersRepository charactersRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    protected String getTACDId(){
        return "123456789012345678901234";
    }

    protected User getTACSTestUserVO() {
        String id = getTACDId();
        User user = new User("TACS", "testPass123;");
        User.validateUser(user);
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        return user;
    }

    protected User getTACSAdminTestUserVO() {
        User user = getTACSTestUserVO();
        user.setIsAdmin(true);
        return user;
    }

    protected Thumbnail getTACSTestThumbnailVO() {
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setPath("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784");
        thumbnail.setExtension("JPG");
        return thumbnail;
    }

    protected Character getTACSTestCharacterVO() {
        Character character = new Character();
        Thumbnail thumbnail = getTACSTestThumbnailVO();
        character.setThumbnail(thumbnail);
        character.setId(1011334);
        character.setName("3-D Man");
        return character;
    }

    protected Team getTACSTestTeamVO() {
        Team team = new Team();
        team.setTeamName("uno");
        return team;
    }

    protected Team getTACSTestTeamWithMemberVO() {
        Team team = getTACSTestTeamVO();
        team.setTeamName("uno");
        team.addMember(createTACSTestCharacter());
        return team;
    }

    protected User createTACSTestUser() {
        User user = getTACSTestUserVO();
        ds.getDatastore().save(user);
        return user;
    }

    protected Token createAndLogInTACSTestUserWithTeam(Team team) {
        User user = getTACSTestUserVO();
        user.addNewTeam(team);
        ds.getDatastore().save(user);
        user.setUserPassword("testPass123;");
        return authRepository.login(user);
    }

    protected Token createAndLogInTACSTestUserWithFavorite(Character character) {
        User user = getTACSTestUserVO();
        user.addAsFavorite(character);
        ds.getDatastore().save(user);
        user.setUserPassword("testPass123;");
        return authRepository.login(user);
    }

    protected User createTACSAdminTestUser() {
        User user = getTACSAdminTestUserVO();
        ds.getDatastore().save(user);
        return user;
    }

    protected Token createAndLogInTACSTestUser() {
        User user = createTACSTestUser();
        user.setUserPassword("testPass123;");
        return authRepository.login(user);
    }

    protected Token createAndLogInTACSTestUserWithNotFreshToken() {
        Token token = createAndLogInTACSTestUser();
        return setNotFreshExpirationDateToToken(token);
    }

    protected Token setNotFreshExpirationDateToToken(Token token) {
        token.setExpirationDate(new Date(new Date().getTime() - 1));
        ds.getDatastore().save(token);
        return token;
    }

    protected Token createAndLogInTACSAdminTestUser() {
        User user = createTACSAdminTestUser();
        user.setUserPassword("testPass123;");
        return authRepository.login(user);
    }

    protected Team createTACSTestTeamWithMember() {
        return teamsRepository.save(getTACSTestTeamWithMemberVO());
    }

    protected Character createTACSTestCharacter() {
        return charactersRepository.save(getTACSTestCharacterVO());
    }

    protected void deleteTACSTestUser() {
        ds.getDatastore().delete(getTACSTestUser());
    }

    protected void deleteTACSTestUserToken() {
        Token token = getTACSTestUserToken();
        ds.getDatastore().delete(token);
    }

    protected void deleteTACSTestUserWithToken() {
        deleteTACSTestUserToken();
        ds.getDatastore().delete(getTACSTestUser());
    }

    protected void deleteTACSTestCharacter() {
        Character character = getTACSTestCharacter();
        Thumbnail thumbnail = getTACSTestThumbnail();
        ds.getDatastore().delete(thumbnail);
        ds.getDatastore().delete(character);
    }

    protected void deleteTACSTestTeamWithMember() {
        deleteTACSTestCharacter();
        Team team = ds.getDatastore().find(Team.class, "teamName", "uno").get();
        ds.getDatastore().delete(team);
    }

    protected Token getTACSTestUserToken() {
        return ds.getDatastore().find(Token.class, "userId", getTACSTestUser().getUserId()).get();
    }

    protected User getTACSTestUser() {
        return ds.getDatastore().find(User.class, "userName", "TACS").get();
    }

    protected Character getTACSTestCharacter() {
        return ds.getDatastore().find(Character.class, "id", 1011334).get();
    }

    protected Thumbnail getTACSTestThumbnail() {
        return ds.getDatastore().find(Thumbnail.class, "path", "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784").get();
    }

}