import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;


public class QueryHandler {

    /** Remembers the documents IDs used to create the index.**/
    private DataSet dataSet;
    private Logger logger = Logger.getLogger(QueryHandler.class.getName());

    public QueryHandler(DataSet dataSet){
        this.dataSet = dataSet;
    }

    //TODO: return pairs (docId, score)
    public ArrayList<String> retrieveDocumentsForQuery(Query query) {
        
        if (query.getTerms().isEmpty()) {
            return new ArrayList<String>();
        }
        
        return handleANDQuery(query);
    }


    /**
     * TODO(vcarbune): Update this method to combine the proper information.
     *
     * It searches from every term given in the query and for each matched document
     * and term it creates a map from DocIds to another Map<Term, Frequency>. 
     *
     * @param query The query object
     * @return a Map from DocIds to another map of terms to positions. 
     */
    private HashMap<String, HashMap<String, TreeSet<Integer>>>
            getDocIdsMatchingPhrase(Query query) {
        ArrayList<String> terms = query.getTerms();
        HashMap<String, HashMap<String, TreeSet<Integer>>> documents =
            new HashMap<String, HashMap<String, TreeSet<Integer>>>();

        for(String term : terms) {
            TreeSet<DocIdEntry> docIDs = dataSet.getDocIdEntrySet(term);
            if(docIDs == null)
                return  documents;
            Iterator<DocIdEntry> iterator = docIDs.iterator();

            while(iterator.hasNext()){
                DocIdEntry currEntry = iterator.next();

                HashMap<String, TreeSet<Integer>> termToPos = documents.get(currEntry.getDocId());
                if(termToPos == null) {
                    termToPos = new HashMap<String, TreeSet<Integer>>();
                    documents.put(currEntry.getDocId(), termToPos);
                }

                termToPos.put(term, currEntry.getPositions());
            }
        }

        // Ensure all the documents have all the terms. Removes the invalid documents.
        HashMap<String, HashMap<String, TreeSet<Integer>>> result =
            new HashMap<String, HashMap<String, TreeSet<Integer>>>();

        for (String documentId : documents.keySet()) {
            if (documents.get(documentId).keySet().size() == query.getTerms().size()) {
                result.put(documentId, documents.get(documentId));
            }
        }

        return result;
    }

    /**
     * Answers queries that contain only AND operators
     *
     * @param query
     * @return A list of documents that map the query
     */
    public ArrayList<String> handleANDQuery(Query query) {
        TreeSet<String> matchingDocs =

            new TreeSet<String>(dataSet.getDocIdSet(query.getTerm(0)));
        for (String term: query.getTerms()) {
            matchingDocs.retainAll(dataSet.getDocIdSet(term));
        }

        return new ArrayList<String>(matchingDocs);
    }
}