package vo;

import java.util.List;

/**
 * Created by pgallazzi on 31/3/16.
 */
public class PocVo {

    private int id;
    private String name;
    private String surname;
    private int age;
    private List<String> hobbies;

    public PocVo(int id, String name, String surname, int age, List<String> hobbies) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.hobbies = hobbies;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

}
