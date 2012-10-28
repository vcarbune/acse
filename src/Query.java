
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Query {

    public static enum Type {

        BASIC,
        LOCAL,
        GLOBAL
    };
    private final static String TYPE_START = "^";
    private Crawler crawler;
    private Type type = Type.BASIC;
    private HashMap<String, Double> termCounts;
    private Stemmer stemmer;

    public Query(Crawler crawler, String query) {
        if (Config.enableStemming) {
            stemmer = new Stemmer();
        }

        termCounts = new HashMap<String, Double>();
        this.crawler = crawler;

        query = query.replaceAll("[^a-zA-Z]", " ");
        Scanner scanner = new Scanner(query);
        parseTerms(scanner);
    }

    public void setType(int queryType) {
        switch (queryType) {
            case 1:
                type = Type.LOCAL;
                break;
            case 2:
                type = Type.GLOBAL;
                break;
            default:
                type = Type.BASIC;
        }
    }
    
    /**
     * Initializes an empty BASIC query. This is NOT a copy constructor.
     *
     * @param query used ONLY to inherit the crawler and the stemmer
     */
    public Query(final Query query) {
        this.stemmer = query.stemmer;
        this.termCounts = new HashMap<String, Double>();
        this.crawler = query.crawler;
        this.type = Type.BASIC;
    }

    private void parseTerms(Scanner scanner) {
        while (scanner.hasNext()) {
            String token = scanner.next().toUpperCase();
            addTerm(token);
        }
    }

    public Type getType() {
        return type;
    }

    public void addTerm(String token) {
        if (crawler.getStopWords() != null && crawler.getStopWords().contains(token)) {
            return;
        }

        if (Config.enableStemming) {
            stemmer.add(token.toLowerCase().toCharArray(), token.length());
            stemmer.stem();
            token = stemmer.toString().toUpperCase();
        }

        Double count = termCounts.get(token);
        if (count == null) {
            termCounts.put(token, 1.0);
        } else {
            termCounts.put(token, count + 1);
        }
    }

    public void putTermWithCount(String term, Double count) {
        termCounts.put(term, count);
    }

    public Set<Map.Entry<String, Double>> getTermCounts() {
        return termCounts.entrySet();
    }

    public Set<String> getTerms() {
        return termCounts.keySet();
    }

    public boolean hasTerm(String term) {
        return termCounts.get(term) != null;
    }
}
