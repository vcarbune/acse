
import java.util.ArrayList;
import java.util.HashSet;

public class KMeans {

    private int iterations;
    private int clusterNo;
    private ArrayList<DocEntry> docEntries;
    private ArrayList<DocEntry> centroids;
    private ArrayList<Integer> documentClusters;
    private ArrayList<ArrayList<Integer>> clusters;

    public KMeans(int iterations, int clusterNo, final ArrayList<DocEntry> docEntries) {
        this.iterations = iterations;
        this.clusterNo = clusterNo;
        this.docEntries = docEntries;
        this.centroids = new ArrayList<DocEntry>(clusterNo);
        this.documentClusters = new ArrayList<Integer>();
        this.clusters = new ArrayList<ArrayList<Integer>>();

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
        computeClusters();
    }

    private void runOneIteration() {
        for (DocEntry docEntry : docEntries) {
            for (int i = 0; i < centroids.size(); i++) {
                
            }
        }
    }

    private void computeClusters() {
    }

    public int getDocumentCluster(int i) {
        return documentClusters.get(i);
    }
}
