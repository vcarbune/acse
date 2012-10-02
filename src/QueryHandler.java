
import java.util.ArrayList;
import java.util.TreeSet;


public class QueryHandler {

    /** Remembers the documents IDs used to create the index.**/
    private TreeSet<String> docIDs;
    private DataSet dataSet;


    public QueryHandler(TreeSet<String> docs, DataSet postingLists){
        this.docIDs = docs;
        this.dataSet = postingLists;
    }


    public ArrayList<String> retrieveDocumentsForQuery(Query query) {
        switch(query.getType()){
        case NOT:
            return handleNOTQuery(query);
        case AND:
            return handleANDQuery(query);
        case OR:
            return handleORQuery(query);
        }

        //TODO: logging?

        return null;
    }

    /**
     * It answers queries that contain only NOT operators
     *
     * @param query - The NOT Query that needs to be answered
     * @return A list of documents that map the query
     */
    public ArrayList<String> handleNOTQuery(Query query){

        TreeSet<String> matchingDocs = new TreeSet<String>();
        matchingDocs.addAll(docIDs);

        ArrayList<String> terms = query.getTerms();
        for(String term:terms){
            TreeSet<String> matchedDocs = dataSet.getDocIdSet(term);
            matchingDocs.removeAll(matchedDocs);
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

    public void setDocuments(TreeSet<String> docs) {
        this.docIDs = docs;
    }

}
