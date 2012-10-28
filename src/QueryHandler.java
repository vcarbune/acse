
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class QueryHandler {

    /**
     * Remembers the documents IDs used to create the index.*
     */
    private DataSet dataSet;

    public QueryHandler(DataSet dataSet) {
        this.dataSet = dataSet;
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
        
        for (Map.Entry<String, Integer> termCount : query.getTermCounts()) {
            double qi = dataSet.computeQueryWeight(termCount.getKey(), termCount.getValue());
            queryVectorLength += qi * qi;
        }
        
        queryVectorLength = Math.sqrt(queryVectorLength);

        for (String docId : docIdSet) {
            double score = 0;

            for (Map.Entry<String, Integer> termCount : query.getTermCounts()) {
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
        HashMap<String, Integer> RLTermCount = dataSet.getCentroid(relevantDocSet);
        HashMap<String, Integer> NRTermCount = dataSet.getCentroid(nonRelevantDocSet);
                
        // Compute the vector of the centroid.
        Set<Map.Entry<String, Integer>> queryTermCount = query.getTermCounts();
        Iterator<Map.Entry<String, Integer>> queryTermIterator = queryTermCount.iterator();
        
        // TODO(vcarbune): Move alpha, beta, gamma to Config class.
        double alpha = 0.5;
        double beta = 0.5;
        double gamma = 0.5;
        
        while (queryTermIterator.hasNext()) {
            Map.Entry<String, Integer> entry = queryTermIterator.next();
            
            Integer queryFrequency = entry.getValue();
            Integer relevantFrequency = RLTermCount.get(entry.getKey());
            if (relevantFrequency == null)
                relevantFrequency = 0;
            
            Integer nonRelevantFrequency = NRTermCount.get(entry.getKey());
            if (nonRelevantFrequency == null)
                nonRelevantFrequency = 0;

            query.putTermWithCount(entry.getKey(),
                    alpha * queryFrequency + beta * relevantFrequency - gamma * nonRelevantFrequency);
        }
        
        return null;
    }
}