
import java.util.Map;
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
}