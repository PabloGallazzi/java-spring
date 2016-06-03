package repositories;

import domain.Token;
import domain.User;
import exceptions.rest.BadRequestException;
import exceptions.rest.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import services.DSMongoInterface;
import spring.utils.ScopesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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
        Token aToken = ds.getDatastore().find(Token.class, "accessToken", accessToken).get();
        if (aToken == null || !aToken.isFresh()) {
            throw new InvalidTokenException("invalid_token");
        }
        return aToken;
    }

    public Token login(User aUser) {
        User user = users.findByUserNameAndPassword(aUser.getUserName(), aUser.getUserPassword());
        if (user == null) {
            throw new BadRequestException("Unable to authenticate user", "invalid_credentials");
        }
        Token token = ds.getDatastore().find(Token.class, "userId", user.getUserId()).get();

        if (token != null) {
            if (token.isFresh()) {
                user.setLastAccess(new Date());
                users.update(user);
                return token;
            } else {
                ds.getDatastore().delete(token);
            }
        }
        List<String> scopes = new ArrayList<>();
        scopes.add(ScopesHelper.READ);
        scopes.add(ScopesHelper.WRITE);
        if (user.isAdmin()) {
            scopes.add(ScopesHelper.ADMIN);
        }
        token = new Token(scopes, user.getUserId());
        ds.getDatastore().save(token);
        user.setLastAccess(new Date());
        users.update(user);
        return token;
    }


}
