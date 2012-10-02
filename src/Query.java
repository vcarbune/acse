
import java.util.ArrayList;
import java.util.Scanner;

public class Query {

    public static enum Type {

        AND,
        OR,
        NOT
    };
    private Type type;
    private ArrayList<String> terms;

    public Query(String query) {
        
        terms = new ArrayList<String>();
        
        Scanner scanner = new Scanner(query);
        String token = scanner.next();
       
        

        try {
            type = Type.valueOf(token);
        } catch (IllegalArgumentException e) {
            terms.add(token);
            token = scanner.next();
            type = Type.valueOf(token);
        }

        while (scanner.hasNext()) {
            token = scanner.next();
            terms.add(token);
            if (scanner.hasNext()) {
                scanner.next();
            }
        }
    }

    public Type getType() {
        return type;
    }

    private void setType(Type type) {
        this.type = type;
    }

    public String getTerm(int i) {
        return terms.get(i);
    }
    
    public ArrayList<String> getTerms() {
        return terms;
    }

    private void addTerms(String term) {
        terms.add(term);
    }
}