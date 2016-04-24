package domain;

import org.mongodb.morphia.annotations.Id;

/**
 * Created by pgallazzi on 15/4/16.
 */
public class Thumbnail {
    @Id
    String path;
    String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
