
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryHandler {

    private final static String TYPE_NOUN = "NoC";
    /**
     * Remembers the documents IDs used to create the index.*
     */
    private DataSet dataSet;
    private HashSet nouns;

    public QueryHandler(String nounsFile, DataSet dataSet) {
        if (nounsFile == null) {
            nouns = null;
        } else {
            nouns = new HashSet();
            parseNounsFile(nounsFile);
        }

        this.dataSet = dataSet;
    }

    private void parseNounsFile(String nounsFile) {
        Scanner file = null;
        try {
            file = new Scanner(new BufferedInputStream(new FileInputStream(nounsFile)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (file.hasNextLine()) {
            Scanner line = new Scanner(file.nextLine());
            String word = line.next();
            String type = line.next();
            if (type.equals(TYPE_NOUN)) {
                nouns.add(word);
            }
        }
    }

    /**
     * Return the list of documents matching the query and their scores
     *
     * @param query
     * @return
     */
    public TreeSet<QueryResult> retrieveDocumentsForQuery(final Query query) {

        TreeSet<QueryResult> docSet = new TreeSet<QueryResult>();

        if (query.getTermCounts().isEmpty()) {
            return docSet;
        }

        TreeSet<String> docIdSet = dataSet.getDocSet();

        if (docIdSet.isEmpty()) {
            return docSet;
        }

        double queryVectorLength = 0;

        for (Map.Entry<String, Double> termCount : query.getTermCounts()) {
            double qi = dataSet.computeQueryWeight(termCount.getKey(), termCount.getValue());
            queryVectorLength += qi * qi;
        }

        queryVectorLength = Math.sqrt(queryVectorLength);

        for (String docId : docIdSet) {
            double score = 0;

            for (Map.Entry<String, Double> termCount : query.getTermCounts()) {
                double qi = dataSet.computeQueryWeight(termCount.getKey(), termCount.getValue());
                double di = dataSet.computeDocWeight(docId, termCount.getKey());
                score += qi * di;
            }

            if (score > 0) {
                score /= queryVectorLength * dataSet.getDocLength(docId);
                docSet.add(new QueryResult(docId, score));
            }
        }

        return docSet;
    }

    private TreeSet<QueryResult> retrieveDocumentsWithGlobalExpansion(final Query query) {
        return null;
    }
}