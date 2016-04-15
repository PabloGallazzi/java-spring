package domain.vo.getmarvelcharacters;

/**
 * Created by pgallazzi on 15/4/16.
 */
public class GetMarvelCharacters {

    int code;
    String status;
    Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
