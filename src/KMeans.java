
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class KMeans {

    private int iterations;
    private int clusterNo;
    private ArrayList<DocEntry> docEntries;
    private ArrayList<DocEntry> centroids;
    private ArrayList<ArrayList<DocEntry>> clusters;
    private ArrayList<Integer> documentClusters;

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

    //TODO: (lori) test it!
    public double computePurity(){
        Iterator<ArrayList<DocEntry>> it = clusters.iterator();
        int numerator = 0;

        while(it.hasNext()){
            int spam = 0;
            int ham = 0;
            ArrayList<DocEntry> cluster = it.next();
            for(DocEntry doc: cluster){
                if(doc.isSpam())
                    spam ++;
                else 
                    ham++;
            }

            int majority = Math.max(spam, ham);
            numerator += majority;
        }

        int denominator = docEntries.size();
        double rez = ((double) numerator) / denominator;

        return rez;
    }


    //TODO:(lori) test it!
    public double computeRandIndex(){
        int next = 1;
        //similar documents same cluster
        int TP = 0;
        //documents that are not similar are in different clusters
        int TN = 0;
        for(Integer clusterID : documentClusters){
            int index = next;
            boolean currentSpam = docEntries.get(next -1).isSpam();
            Iterator<Integer> it = documentClusters.listIterator(next);

            while(it.hasNext()){ 
                int nextClusterID = it.next(); 
                boolean otherSpam = docEntries.get(index).isSpam(); 
                if( clusterID == nextClusterID && currentSpam == otherSpam){
                    TP++; 
                } else if(clusterID != nextClusterID && currentSpam != otherSpam){
                    TN++;
                }
            }

            next++;
        }

        int nominator = TP + TN; 
        int nrDocs = docEntries.size(); 
        int denominator = nrDocs * (nrDocs -1 )/2;

        double randIndx = ((double)nominator)/denominator; 

        return randIndx;
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
