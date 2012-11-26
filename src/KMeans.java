
import java.util.ArrayList;
import java.util.HashSet;

public class KMeans {

    private int iterations;
    private int clusterNo;
    private ArrayList<DocEntry> docEntries;
    private ArrayList<DocEntry> centroids;
    private ArrayList<Integer> documentClusters;
    private ArrayList<ArrayList<DocEntry>> clusters;

    public KMeans(int iterations, int clusterNo, final ArrayList<DocEntry> docEntries) {
        this.iterations = iterations;
        this.clusterNo = clusterNo;
        this.docEntries = docEntries;
        this.centroids = new ArrayList<DocEntry>(clusterNo);
        this.documentClusters = new ArrayList<Integer>(docEntries.size());
        this.clusters = new ArrayList<ArrayList<DocEntry>>(clusterNo);
        
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
        // Init
        for (int i = 0; i < docEntries.size(); i++) {
            documentClusters.add(-1);
        }
        for (int i = 0; i < clusterNo; i++) {
            clusters.add(new ArrayList<DocEntry>());
        }
        
        // Actual run
        for (int i = 0; i < iterations; i++) {
            runOneIteration();
        }
        computeClusters();
    }

    private void runOneIteration() {
        // Compute centroids for each document
        for (int d = 0; d < docEntries.size(); d++) {
            int bestK = -1;
            double bestDist = Integer.MAX_VALUE;
            
            for (int k = 0; k < centroids.size(); k++) {
                double dist = docEntries.get(d).getDistance(centroids.get(k));
                if (dist < bestDist) {
                    bestDist = dist;
                    bestK = k;
                }
            }
            
            documentClusters.set(d, bestK);
            d++;
        }
        
        // Compute new centroids
        for (int k = 0; k < centroids.size(); k++) {
            centroids.set(k, new DocEntry());
        }
        for (int d = 0; d < docEntries.size(); d++) {
            centroids.get(documentClusters.get(d)).addWordWeights(docEntries.get(d));
        }
    }

    private void computeClusters() {
        for (int d = 0; d < docEntries.size(); d++) {
            clusters.get(documentClusters.get(d)).add(docEntries.get(d));
        }
    }

    public ArrayList<DocEntry> getCluster(int index) {
        return clusters.get(index);
    }

    public int getDocumentCluster(int i) {
        return documentClusters.get(i);
    }
}
