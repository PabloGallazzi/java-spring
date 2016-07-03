package spring.utils;

import java.util.Date;

/**
 * Created by PabloGallazzi on 2/7/16.
 */
public class Cacheable {

    Object data;
    Date expiresIn;

    public Cacheable(Object data) {
        this.data = data;
        Date date = new Date();
        date.setTime(date.getTime() + 21600000L);
        this.expiresIn = date;
    }

    public boolean isValid() {
        return this.expiresIn.after(new Date());
    }

    public Object getData() {
        return data;
    }
}