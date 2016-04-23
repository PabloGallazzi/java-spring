package domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("characters")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Character {
    @Id
    private Integer id;
    String name;
    String description;
    Integer electedTimes = 0;
    Thumbnail thumbnail;
    String resourceURI;
    private String eTag;

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getElectedTimes() {
        return electedTimes;
    }

    public void setElectedTimes(Integer electedTimes) {
        this.electedTimes = electedTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character() {/*Necessary for Mongo*/}

    public Character(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public void selectedAsFavorite(){
        electedTimes++;
    }

    public void removedAsFavorite(){
        electedTimes--;
    }
}
