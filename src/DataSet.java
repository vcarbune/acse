import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Logger;

public class DataSet {
	private HashMap<String, TreeSet<String>> data = new HashMap<String, TreeSet<String>>();
	private Logger logger = Logger.getLogger(DataSet.class.getName());
	
	/**
	 * Returns the list of documents ids in which a specific term appears.
	 * 
	 * @param term The term for which to return the doc id list.
	 * @return The list of document ids.
	 */
	public TreeSet<String> getDocIdList(String term) {
		return data.get(term);
	}
	
	/**
	 * Marks the appearance of a term in a docId.
	 * 
	 * @param term The term discovered.
	 * @param docId The document id in which it appears.
	 */
	public void addPair(String term, String docId) {
		TreeSet<String> docIdList = data.get(term);

		if (docIdList == null) {
			docIdList = new TreeSet<String>();
			data.put(term, docIdList);
		}
		
		if (!docIdList.contains(docId)) {
			docIdList.add(docId);
		}
	}

	/**
	 * Logs the statistics related to the corpus.
	 */
	public void logStaticStats() {
		logger.log(Config.LOG_LEVEL, "Generating Statistics from the Document Corpus");
	}
}