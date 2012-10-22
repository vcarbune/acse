import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;


public class QueryHandler {

    /** Remembers the documents IDs used to create the index.**/
    private DataSet dataSet;
    private Logger logger = Logger.getLogger(QueryHandler.class.getName());

    public QueryHandler(DataSet dataSet){
        this.dataSet = dataSet;
    }

    /**
     * Return the list of documents matching the query and their scores
     * @param query
     * @return
     */
    public TreeSet<QueryResult> retrieveDocumentsForQuery(Query query) {
        
        TreeSet<QueryResult> docSet = new TreeSet<QueryResult>();
        
        if (query.getTerms().isEmpty()) {
            return docSet;
        }
        
        ArrayList<String> docIdList = getMatchingDocs(query);
        
        double queryVectorLength = 0;
        
        for (String term: query.getTerms()) {
            double qi = dataSet.computeQueryWeight(query, term);
            queryVectorLength += qi * qi;
        }
        
        queryVectorLength = Math.sqrt(queryVectorLength);
        
        for (String docId: docIdList) {
            
            double score = 0;
            double docVectorLength = 0;
            
            for (String term: query.getTerms()) {
                double qi = dataSet.computeQueryWeight(query, term);
                double di = dataSet.computeDocWeight(docId, term);
                docVectorLength += di * di;
                score += qi * di;
            }
            
            docVectorLength = Math.sqrt(docVectorLength);
            
            score /= queryVectorLength * docVectorLength;
            
            docSet.add(new QueryResult(docId, score));
        }
        
        return docSet;
    }
    
    private ArrayList<String> getMatchingDocs(Query query) {
        TreeSet<String> matchingDocs =
            new TreeSet<String>(dataSet.getDocIdSet(query.getTerm(0)));
        
        for (String term: query.getTerms()) {
            matchingDocs.retainAll(dataSet.getDocIdSet(term));
        }

        return new ArrayList<String>(matchingDocs);
    }
}