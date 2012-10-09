import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;


public class QueryHandler {

    /** Remembers the documents IDs used to create the index.**/
    private DataSet dataSet;
    private Logger logger = Logger.getLogger(QueryHandler.class.getName());

    public QueryHandler(DataSet dataSet){
        this.dataSet = dataSet;
    }

    public ArrayList<String> retrieveDocumentsForQuery(Query query) {
        
        switch(query.getType()){
        case NOT:
            return handleNOTQuery(query);
        case AND:
            return handleANDQuery(query);
        case OR:
            return handleORQuery(query);
        case PHRASE:
            return handlePhraseQuery(query);
        case PROXIMITY:
            return handleProximityQuery(query);
        }

        logger.log(Config.LOG_LEVEL, "Query type " + query.getType() + " not recognized!");

        return null;
    }

    private TreeSet<DocIdEntry> retrieveDocumentEntriesForQuery(Query query)
    {
        // Note: This behaves exactly as handleANDQuery, but returns a DocIdEntry.
        // This is not useful for NOT and OR queries.
        if (query.getType() == Query.Type.NOT || query.getType() == Query.Type.OR) {
            return null;
        }
        
        TreeSet<DocIdEntry> matchingDocs =
                new TreeSet<DocIdEntry>(dataSet.getDocIdEntry(query.getTerm(0)));

        for (String term: query.getTerms()) {
            matchingDocs.retainAll(dataSet.getDocIdEntry(term));
        }

        return matchingDocs;
    }

    /**
     * Answers proximity queries
     *
     * @param query
     * @return A list of documents that map the query
     */
    private ArrayList<String> handleProximityQuery(Query query) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Answers phrase queries
     *
     * @param query
     * @return A list of documents that map the query
     */
    private ArrayList<String> handlePhraseQuery(Query query) {
        ArrayList<String> terms = query.getTerms();
        TreeSet<DocIdEntry> documents = new TreeSet<DocIdEntry>();
        
        // Iterate through all the documents of each terms, increase
        // artificially the appearance positions by 1. In the end,
        // retains the documents from followings terms only if they
        // match at least one artificially increased position.
        
        /*
        TreeSet<DocIdEntry> docIdEntries = dataSet.getDocIdEntry(terms.get(0));
        for (DocIdEntry entry : docIdEntries) {
            TreeSet<Integer> positions = new TreeSet<Integer>();
            for (Integer position : entry.getPositions()) {
                positions.add(position + 1);
            }
            
            documents.add(new DocIdEntry(entry.getDocId(), positions));
        }
        
        for (int i = 1; i < terms.size(); ++i) {
            String term = terms.get(i);
            docIdEntries = dataSet.getDocIdEntry(term);
            
            for (DocIdEntry entry : docIdEntries) {
                DocIdEntry existingEntry = documents.lower(entry);
                if (existingEntry == null || !existingEntry.getDocId().equals(entry.getDocId())) {
                    continue;
                }
            }
        }
        */      
        return null;
    }

    /**
     * It answers queries that contain only NOT operators
     *
     * Queries can be of form: A NOT B. The result returned
     * is of all documents containing A and that do not contain B.
     * In case the query is: NOT A. The method returns
     * all documents that do not contain A.
     *
     * @param query - The NOT Query that needs to be answered
     * @return A list of documents that map the query
     */
    public ArrayList<String> handleNOTQuery(Query query){

        TreeSet<String> matchingDocs = new TreeSet<String>();

        ArrayList<String> terms = query.getTerms();
        for(String term:terms){
            TreeSet<String> matchedDocs = dataSet.getDocIdSet(term);
            matchingDocs.addAll(matchedDocs);
        }

        if(matchingDocs.isEmpty()){
            matchingDocs = dataSet.getDocSet();
        }

        ArrayList<String> notTerms = query.getNotTerms();
        for(String notTerm : notTerms){
            matchingDocs.removeAll(dataSet.getDocIdSet(notTerm));
        }


        return new ArrayList<String>(matchingDocs);
    }

    /**
     * Answers queries that contain only AND operators
     *
     * @param query
     * @return A list of documents that map the query
     */
    public ArrayList<String> handleANDQuery(Query query) {

        // TODO: We could define different orders. For each permutation, we could have an order.
        // We first add the list for the first term, and then intersect with the others.

        TreeSet<String> matchingDocs =
            new TreeSet<String>(dataSet.getDocIdSet(query.getTerm(0)));

        for (String term: query.getTerms()) {
            matchingDocs.retainAll(dataSet.getDocIdSet(term));
        }

        return new ArrayList<String>(matchingDocs);
    }

    /**
     * Answers queries that contain only OR operators
     *
     * @param query
     * @return A list of documents that map the query
     */
    public ArrayList<String> handleORQuery(Query query) {

        // TODO: We could define different orders. For each permutation, we could have an order.
        // We first add the list for the first term, and then union with the others.

        TreeSet<String> matchingDocs = new TreeSet<String>();

        for (String term: query.getTerms()) {
            matchingDocs.addAll(dataSet.getDocIdSet(term));
        }

        return new ArrayList<String>(matchingDocs);
    }
}
