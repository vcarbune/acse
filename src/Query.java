
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
    private static String PHRASE_DELIMITER = "\"";
    private static String PROXIMITY_DELIMITER = "\\";
    private Crawler crawler;
    private Type type;
    private int proximity_window;
    private ArrayList<String> terms;
    private ArrayList<String> notTerms;

    public Query(Crawler crawler, String query) {
        this.crawler = crawler;

        proximity_window = Integer.MAX_VALUE;
        terms = new ArrayList<String>();
        notTerms = new ArrayList<String>();

        Scanner scanner = new Scanner(query);
        String token = scanner.next().toUpperCase();

        if (maybeSetPhrase(token) || maybeSetProximity(token)) {
            parseImplicitAndTerms(scanner);
        } else {
            setBoolean(token, scanner);
            parseFullBooleanTerms(scanner);
        }
    }

    private boolean maybeSetPhrase(String token) {
        if (token.startsWith(PHRASE_DELIMITER)) {
            type = Type.PHRASE;
            addTerm(token.substring(1), true);

            return true;
        }

        return false;
    }

    private boolean maybeSetProximity(String token) {
        if (token.startsWith(PROXIMITY_DELIMITER)) {
            type = Type.PROXIMITY;
            Scanner s = new Scanner(token.substring(1));
            proximity_window = s.nextInt();

            return true;
        }

        return false;
    }

    private void setBoolean(String token, Scanner scanner) {
        try {
            type = Type.valueOf(token);

            // We might encounter PHRASE and PROXIMITY as words.
            // We don't want them to be parsed as the query Type.
            if (type == Type.PHRASE || type == Type.PROXIMITY) {
                // HACK WARNING: This code is a hack.
                type = null;
                throw new IllegalArgumentException("HACK for" + token);
            }
        } catch (IllegalArgumentException e) {
            addTerm(token, true);
            if (!scanner.hasNext()) { // If we have only one term
                type = Type.AND;
            } else {
                token = scanner.next().toUpperCase();
                type = Type.valueOf(token);
            }
        }
    }

    private void parseImplicitAndTerms(Scanner scanner) {
        while (scanner.hasNext()) {
            String token = scanner.next().toUpperCase();
            addTerm(token, true);
        }

        if (type == Type.PHRASE) {
            String token = terms.remove(terms.size() - 1);
            if (token.endsWith(PHRASE_DELIMITER)) {
                token = token.substring(0, token.length() - 1);
            }
            addTerm(token, true);
        }
    }

    private void parseFullBooleanTerms(Scanner scanner) {
        Type lastOperator = type;
        while (scanner.hasNext()) {
            String token = scanner.next().toUpperCase();

            if (lastOperator == Type.NOT) {
                addTerm(token, false);
            } else {
                addTerm(token, true);
            }

            if (scanner.hasNext()) {
                lastOperator = Type.valueOf(scanner.next().toUpperCase());
            }
        }
    }

    public Type getType() {
        return type;
    }

    public int getProximityWindow() {
        return proximity_window;
    }

    private void addTerm(String token, boolean positiveTerm) {
        if (crawler.getStopWords() != null && crawler.getStopWords().contains(token)) {
            return;
        }

        if (positiveTerm) {
            terms.add(token);
        } else {
            notTerms.add(token);
        }
    }

    public String getTerm(int i) {
        return terms.get(i);
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public ArrayList<String> getNotTerms() {
        return notTerms;
    }
}