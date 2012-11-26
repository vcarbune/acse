
import java.util.ArrayList;
import java.util.HashSet;

public class KMeans {

    private int iterations;
    private int clusterNo;
    private ArrayList<DocEntry> docEntries;
    private ArrayList<DocEntry> centroids;
    private ArrayList<ArrayList<DocEntry>> clusters;
    // FIXME: remove
    //private ArrayList<Integer> documentClusters;

    public KMeans(int iterations, int clusterNo, final ArrayList<DocEntry> docEntries) {
        this.iterations = iterations;
        this.clusterNo = clusterNo;
        this.docEntries = docEntries;
        this.centroids = new ArrayList<DocEntry>(clusterNo);
        // FIXME: remove
        //this.documentClusters = new ArrayList<Integer>();
        this.clusters = new ArrayList<ArrayList<DocEntry>>();

        initCentroids();
    }

    private void initCentroids() {
        int n = docEntries.size();
        HashSet<Integer> indexes = new HashSet<Integer>();

        for (int k = 0; k < clusterNo; k++) {
            int current;
            do {
                current = (int) Math.floor(Math.random() * n);
            } while (indexes.contains(current));
            indexes.add(current);

            DocEntry docEntry = new DocEntry();
            docEntry.copyWordWeights(docEntries.get(current));
            centroids.add(docEntry);
        }
    }

    public void runAllIterations() {
        for (int i = 0; i < iterations; i++) {
            runOneIteration();
        }
    }

    private void runOneIteration() {
        clusters.clear();

        for (DocEntry docEntry : docEntries) {
            for (int i = 0; i < centroids.size(); i++) {
                
            }
        }
    }

    public ArrayList<DocEntry> getCluster(int index) {
        return clusters.get(index);
    }
    
    /**
     * FIXME: remove
     * @deprecated
     */
    public int getDocumentCluster(int i) {
        //return documentClusters.get(i);
        return -1;
    }
}
