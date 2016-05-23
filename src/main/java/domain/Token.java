package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exceptions.rest.ForbiddenException;
import exceptions.rest.UnauthorizedException;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import spring.utils.ObjectIdToStringSerializer;
import spring.utils.ScopesHelper;

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
    //No need to link the user
    @JsonSerialize(using = ObjectIdToStringSerializer.class)
    ObjectId userId;
    @Embedded
    List<String> scopes;

    public Token() {
    }

    public Token(List<String> scopes, ObjectId userId) {
        Date date = new Date();
        date.setTime(date.getTime() + 15770000000L);
        this.expirationDate = date;
        this.scopes = scopes;
        this.userId = userId;
        Random random = new Random();
        Double doubleValue = random.nextDouble();
        this.accessToken = userId.toString() + "-" + DigestUtils.sha256Hex(doubleValue.toString() + this.expirationDate.toString());
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

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId user) {
        this.userId = user;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    @JsonIgnore
    public Boolean isFresh() {
        return expirationDate.after(new Date());
    }

    public void validateAdminCredentials() {
        if (!this.getScopes().contains(ScopesHelper.ADMIN)) {
            throw new ForbiddenException("Forbidden");
        }
    }

    public void validateUserCredentials(String userId) {
        if (!this.getUserId().toString().equals(userId)) {
            throw new ForbiddenException("Forbidden");
        }
    }

    public static void validateNonEmptyToken(String token) {
        if (token.isEmpty()) {
            throw new UnauthorizedException("Access token must be provided");
        }
    }

}
