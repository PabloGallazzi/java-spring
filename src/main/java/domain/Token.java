package domain;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by pgallazzi on 11/4/16.
 */
public class Token {

    Date expiration_date;
    String access_token;
    Integer user_id;
    List<String> scopes;

    public Token(List<String> scopes, Integer user_id) {
        Date date = new Date();
        date.setTime(date.getTime()+120001);
        this.expiration_date = date;
        this.scopes = scopes;
        this.user_id = user_id;
        Random random = new Random();
        Double doubleValue = random.nextDouble();
        this.access_token = user_id.toString() + "-" + DigestUtils.sha256Hex(doubleValue.toString()) + "-" + DigestUtils.sha256Hex(this.expiration_date.toString());
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String token) {
        this.access_token = token;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user) {
        this.user_id = user;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

}
