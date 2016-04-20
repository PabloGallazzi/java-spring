package domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.codec.digest.DigestUtils;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by pgallazzi on 11/4/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity("tokens")
public class Token {
    @Id
    String accessToken;
    Date expirationDate;
    //@Reference(lazy = true) Si referencias un usuario y no el ID deberias tener esto
    Integer userId;
    @Embedded //Revisar si esto funciona o estalla porque no se si se embeben listas de strings
    List<String> scopes;

    public Token(List<String> scopes, Integer userId) {
        Date date = new Date();
        date.setTime(date.getTime()+120001);
        this.expirationDate = date;
        this.scopes = scopes;
        this.userId = userId;
        Random random = new Random();
        Double doubleValue = random.nextDouble();
        this.accessToken = userId.toString() + "-" + DigestUtils.sha256Hex(doubleValue.toString()) + "-" + DigestUtils.sha256Hex(this.expirationDate.toString());
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user) {
        this.userId = user;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

}
