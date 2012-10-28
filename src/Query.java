
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
    private Type type;
    private HashMap<String, Double> termCounts;
    private Stemmer stemmer;

    public Query(Crawler crawler, String query) {
        if (Config.enableStemming) {
            stemmer = new Stemmer();
        }

        termCounts = new HashMap<String, Double>();
        this.crawler = crawler;

        query = findType(query);
        query = query.replaceAll("[^a-zA-Z]", " ");
        Scanner scanner = new Scanner(query);
        parseTerms(scanner);
    }

    private String findType(String query) {
        type = Type.BASIC;
        int index = query.indexOf(" ");
        String token = query.substring(0, index).toUpperCase();

        if (!token.startsWith(TYPE_START)) {
            return query;
        }

        try {
            type = Type.valueOf(token.substring(1));
        } catch (IllegalArgumentException e) {
            return query;
        }
        
        return query.substring(index + 1, query.length());
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

    private void addTerm(String token) {
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
}
