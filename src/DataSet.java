
import java.util.HashMap;
import java.util.LinkedList;

public class DataSet {

    private HashMap<String, LinkedList<String>> data;

    public LinkedList<String> getDocIdList(String term) {
        return data.get(term);
    }

    public void addPair(String term, String docId) {
        // TODO():
    }
}
