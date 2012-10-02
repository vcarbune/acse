
import java.util.ArrayList;
import java.util.HashSet;
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
        if(query.getType() == Query.Type.NOT){
            return handleNOTQuery(query);
        }
        return null;
    }

    /**
     * It answers queries that contain only NOT operators
     *
     * @param query - The NOT Query that needs to be answered
     * @return A list of documents that map the query
     */
    public ArrayList<String> handleNOTQuery(Query query){
        TreeSet<String> matchingFiles = new TreeSet<String>();
        matchingFiles.addAll(docIDs);
     /* TODO: change the line to  ArrayList<String> terms = query.getTerms();
      *       when Query is changed
       */
        String term = query.getTerm(0);
        TreeSet<String> matchedDocs = dataSet.getDocIdList(term);
        matchingFiles.removeAll(matchedDocs);

        return new ArrayList<String>(matchingFiles);
    }


    public void setDocuments(TreeSet<String> docs) {
        this.docIDs = docs;
    }

}
