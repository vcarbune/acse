
import java.util.ArrayList;
import java.util.HashSet;

public class KMeans {

    private int iterations;
    private int clusterNo;
    private DocSet docSet;
    private ArrayList<DocEntry> centroids;
    private ArrayList<Integer> documentClusters;
    private ArrayList<ArrayList<Integer>> clusters;

    public KMeans(int iterations, int clusterNo, final DocSet docSet) {
        this.iterations = iterations;
        this.clusterNo = clusterNo;
        this.docSet = docSet;
        this.centroids = new ArrayList<DocEntry>(clusterNo);
        this.documentClusters = new ArrayList<Integer>();
        this.clusters = new ArrayList<ArrayList<Integer>>();

        initCentroids();
    }

    private void initCentroids() {
        int n = docSet.getNumTotalDocs();
        HashSet<Integer> indexes = new HashSet<Integer>();

        for (int k = 0; k < clusterNo; k++) {
            int current;
            do {
                current = (int) Math.floor(Math.random() * n);
            } while (indexes.contains(current));
            indexes.add(current);

            DocEntry docEntry = new DocEntry();
            docEntry.copyWordWeights(docSet.getDocEntry(current));
            centroids.add(docEntry);
        }
    }

    public void runAllIterations() {
        for (int i = 0; i < iterations; i++) {
            runOneIteration();
        }
        computeClusters();
    }

    private void runOneIteration() {
        
    }

    private void computeClusters() {
    }

    public ArrayList<DocEntry> getCluster(int index) {
        // TODO(uvictor): Clusterify.
        return null;
    }
    
    public int getDocumentCluster(int i) {
        return documentClusters.get(i);
    }
}
