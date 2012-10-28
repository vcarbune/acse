
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
    private Crawler crawler;
    private Type type;
    private HashMap<String, Integer> termCounts;
    private Stemmer stemmer;

    public Query(Crawler crawler, String query) {
        if (Config.enableStemming) {
            stemmer = new Stemmer();
        }
        
        termCounts = new HashMap<String, Integer>();
        this.crawler = crawler;
        
        findType(query);
        query = query.replaceAll("[^a-zA-Z]", " ");
        Scanner scanner = new Scanner(query);
        parseTerms(scanner);
    }

    private void findType(String query) {
        type = Type.BASIC;
        Scanner scanner = new Scanner(query);
        
        if (scanner.hasNext()) {
            String token = scanner.next().toUpperCase();
            try {
                type = Type.valueOf(token);
            } catch (IllegalArgumentException e) {
                addTerm(token);
            }
        }
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

        Integer count = termCounts.get(token);
        if (count == null) {
            termCounts.put(token, 1);
        } else {
            termCounts.put(token, count + 1);
        }
    }

    public Set<Map.Entry<String, Integer>> getTermCounts() {
        return termCounts.entrySet();
    }

    public Set<String> getTerms() {
        return termCounts.keySet();
    }
}