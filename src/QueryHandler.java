
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;

public class QueryHandler {

    private final static String TYPE_NOUN = "NoC";
    private final static String WORDNET_CONFIG = "";
    /**
     * Remembers the documents IDs used to create the index.*
     */
    private HashSet nouns;
    private Dictionary dictionary;
    private DataSet dataSet;

    public QueryHandler(String nounsFile, DataSet dataSet) {
        if (nounsFile == null) {
            nouns = null;
        } else {
            nouns = new HashSet();
            parseNounsFile(nounsFile);
        }

        try {
            dictionary = Dictionary.getInstance(new FileInputStream(WORDNET_CONFIG));
        } catch (Exception ex) {
            Logger.getLogger(QueryHandler.class.getName()).log(Level.SEVERE, null, ex);
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
        Query expandedQuery = new Query(query);
        
        try {
            for (String term : query.getTerms()) {
                System.out.println("For " + term);
                if (nouns.contains(term)) {
                    IndexWord indexWord = dictionary.getIndexWord(POS.NOUN, term);
                    List<Synset> synsets = indexWord.getSenses();
                    if (synsets.size() > 0) {
                        for (Word word : synsets.get(0).getWords()) {
                            expandedQuery.addTerm(word.getLemma());
                            System.out.println("\tAdded " + word.getLemma());
                        }
                    }
                }
            }
        } catch (JWNLException ex) {
            Logger.getLogger(QueryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}