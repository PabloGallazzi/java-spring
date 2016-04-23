package repositories;

import domain.Token;
import domain.User;
import exceptions.rest.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import services.DSMongoInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 4/19/16.
 */
@Repository
@Scope("singleton")
public class AuthRepository {
    @Autowired
    private DSMongoInterface ds;
    @Autowired
    private UsersRepository users;

    public Token findById(String accessToken) {
        return ds.getDatastore().find(Token.class, "accessToken", accessToken).get();
    }

    public Token login(User aUser) {
        User user = users.findByUserNameAndPassword(aUser.getUserName(), aUser.getUserPassword());
        if (user == null) {
            throw new BadRequestException("Unable to authenticate user", "invalid_credentials");
        }
        Token token = ds.getDatastore().find(Token.class, "userId", user.getUserId()).get();
        if (token != null) {
            if (token.isFresh()) {
                return token;
            } else {
                ds.getDatastore().delete(token);
            }
        }
        List<String> scopes = new ArrayList<>();
        scopes.add("read");
        scopes.add("write");
        token = new Token(scopes,user.getUserId());
        ds.getDatastore().save(token);
        return token;
    }


}
