package domain.vo.getmarvelcharacters;

import domain.Character;

/**
 * Created by pgallazzi on 15/4/16.
 */
public class Data {

    int offset;
    int limit;
    int total;
    Character[] results;

    public Character[] getResults() {
        return results;
    }

    public void setResults(Character[] results) {
        this.results = results;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
