
import java.util.ArrayList;
import java.util.Iterator;
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

        //ArrayList<String> docIdList = getMatchingDocs(query);
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
            double docVectorLength = 0;

            for (Map.Entry<String, Integer> termCount : query.getTermCounts()) {
                double qi = dataSet.computeQueryWeight(termCount.getKey(), termCount.getValue());
                double di = dataSet.computeDocWeight(docId, termCount.getKey());
                docVectorLength += di * di;
                score += qi * di;
            }

            docVectorLength = Math.sqrt(docVectorLength);

            if (docVectorLength != 0) {
                score /= queryVectorLength * docVectorLength;
                docSet.add(new QueryResult(docId, score));
            }
        }

        return docSet;
    }

    private ArrayList<String> getMatchingDocs(final Query query) {
        Iterator<String> term = query.getTerms().iterator();
        TreeSet<String> matchingDocs =
                new TreeSet<String>(dataSet.getDocIdSet(term.next()));

        while (term.hasNext()) {
            matchingDocs.retainAll(dataSet.getDocIdSet(term.next()));
        }

        return new ArrayList<String>(matchingDocs);
    }
}