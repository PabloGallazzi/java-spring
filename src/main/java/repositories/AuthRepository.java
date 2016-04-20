package repositories;

import domain.Character;
import domain.Team;
import domain.Token;
import domain.User;
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

    public Token findById(String accessToken){
        return ds.getDatastore().find(Token.class, "accessToken", accessToken).get();
    }

    public Token login(User aUser){
        User loggedUser = users.findByUserNameAndPassword(aUser.getUserName(), aUser.getUserPassword());
        List<String> scopes = new ArrayList<>();
        scopes.add("admin");
        scopes.add("read");
        scopes.add("write");
        Token token = new Token(scopes,1);
        //Retorna token o null
        return (loggedUser != null) ? new Token(scopes,loggedUser.getUserId()) : null ;
    }


}
