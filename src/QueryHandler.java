import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
    private final static String WORDNET_CONFIG = "file_properties.xml";
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
            String word = line.next().toUpperCase();
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

    private TreeSet<QueryResult> retrieveDocumentsWithLocalExpansion(Query query) {
        // Pseudo-relevance method: assume first document is relevant, while the others are not.
        TreeSet<QueryResult> documents = retrieveDocumentsForQuery(query);
        Iterator<QueryResult> docSetIterator = documents.iterator();

        // Relevant document set (only 1 used, according to the specification)
        ArrayList<String> relevantDocSet = new ArrayList<String>();
        relevantDocSet.add(docSetIterator.next().getDocId());

        // Non-relevant document set (the rest of them).
        ArrayList<String> nonRelevantDocSet = new ArrayList<String>();
        while (docSetIterator.hasNext()) {
            nonRelevantDocSet.add(docSetIterator.next().getDocId());
        }

        // Compute centroids.
        HashMap<String, Double> RLTermCount = dataSet.getCentroid(relevantDocSet);
        HashMap<String, Double> NRTermCount = dataSet.getCentroid(nonRelevantDocSet);

        // Compute the vector of the centroid.
        Set<Map.Entry<String, Double>> queryTermCount = query.getTermCounts();
        Iterator<Map.Entry<String, Double>> queryTermIterator = queryTermCount.iterator();

        while (queryTermIterator.hasNext()) {
            Map.Entry<String, Double> entry = queryTermIterator.next();

            Double queryFrequency = entry.getValue();
            Double relevantFrequency = RLTermCount.get(entry.getKey());
            if (relevantFrequency == null) {
                relevantFrequency = 0.0;
            }
            relevantFrequency /= relevantDocSet.size();

            Double nonRelevantFrequency = NRTermCount.get(entry.getKey());
            if (nonRelevantFrequency == null) {
                nonRelevantFrequency = 0.0;
            }
            nonRelevantFrequency /= nonRelevantDocSet.size();

            Double finalFrequency = Config.alpha * queryFrequency + 
                    Config.beta * relevantFrequency - Config.gamma * nonRelevantFrequency;
            
            if (finalFrequency > 0) {
                query.putTermWithCount(entry.getKey(), finalFrequency);
            }
        }

        for (String term : RLTermCount.keySet()) {
            if (query.hasTerm(term)) {
                continue;
            }

            Double frequency = Config.beta * RLTermCount.get(term);
            if (NRTermCount.get(term) != null) {
                frequency -= Config.gamma * NRTermCount.get(term);
            }

            if (frequency > 0) {
                query.putTermWithCount(term, frequency);
            }
        }

        return retrieveDocumentsForQuery(query);
    }

    private TreeSet<QueryResult> retrieveDocumentsWithGlobalExpansion(final Query query) {
        Query expandedQuery = new Query(query);
        
        try {
            for (String term : query.getTerms()) {
                IndexWord indexWord = dictionary.getIndexWord(POS.NOUN, term.toLowerCase());
                // We use WordNet to test if a word is a noun or not.
                // if (nouns != null && nouns.contains(term) && indexWord != null)
                if (indexWord != null) {
                    List<Synset> synsets = indexWord.getSenses();
                    
                    if (!synsets.isEmpty()) {
                        for (Word word : synsets.get(0).getWords()) {
                            expandedQuery.addTerm(word.getLemma().toUpperCase());
                        }
                    }
                } else {
                    expandedQuery.addTerm(term);
                }
            }
        } catch (JWNLException ex) {
            Logger.getLogger(QueryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retrieveDocumentsForQuery(expandedQuery);
    }

    public TreeSet<QueryResult> retrieveDocument(Query query) {
        if (query.getType().equals(Query.Type.GLOBAL)) {
            return retrieveDocumentsWithGlobalExpansion(query);
        } else if (query.getType().equals(Query.Type.LOCAL)) {
            return retrieveDocumentsWithLocalExpansion(query);
        }

        return retrieveDocumentsForQuery(query);
    }
}
