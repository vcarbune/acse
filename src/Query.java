
import java.util.ArrayList;

public class Query {

    public static enum Type {

        AND,
        OR,
        NOT
    };
    private Type type;
    private ArrayList<String> terms;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTerm(int i) {
        return terms.get(i);
    }

    public void addTerms(String term) {
        terms.add(term);
    }
}