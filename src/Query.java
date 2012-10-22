
import java.util.ArrayList;
import java.util.Scanner;

public class Query {

    public static enum Type {

        AND,
        OR,
        NOT,
        PHRASE,
        PROXIMITY
    };
    private static String QUERY_END = ".";
    private Crawler crawler;
    private Type type;
    private ArrayList<String> terms;
    private Stemmer stemmer;

    public Query(Crawler crawler, String query) {
        this.crawler = crawler;

        if (Config.enableStemming) {
            stemmer = new Stemmer();
        }

        terms = new ArrayList<String>();

        query = query.replaceAll("[^a-zA-Z]", " ");
        Scanner scanner = new Scanner(query);

        type = Type.AND;
        parseTerms(scanner);
    }

    private void parseTerms(Scanner scanner) {
        while (scanner.hasNext()) {
            String token = scanner.next().toUpperCase();
            addTerm(token);
        }

        String token = terms.get(terms.size() - 1);
        if (token.equals(QUERY_END)) {
            terms.remove(terms.size() - 1);
        }
    }

    public Type getType() {
        return type;
    }

    /**
     * @return -1
     * @deprecated
     */
    public int getProximityWindow() {
        return -1;
    }

    private void addTerm(String token) {
        if (crawler.getStopWords() != null && crawler.getStopWords().contains(token)) {
            return;
        }

        if (Config.enableStemming) {
            stemmer.add(token.toLowerCase().toCharArray(), token.length());
            stemmer.stem();
            token = stemmer.toString().toUpperCase();
        }

        terms.add(token);
    }

    public String getTerm(int i) {
        return terms.get(i);
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    /**
     * @return null
     * @deprecated
     */
    public ArrayList<String> getNotTerms() {
        return null;
    }
}