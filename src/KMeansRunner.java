import java.util.ArrayList;

public class KMeansRunner {
    ArrayList<DocSet> docsSets;

    public KMeansRunner(ArrayList<DocSet> docSets) {
        this.docsSets = docSets;
    }

    public void run() {
        for (int K = Config.infK; K < Config.supK; ++K) {
            for (int N = 0; N < Config.N; ++N) {
                for (DocSet docSet : docsSets) {
                    KMeans kMeansInstance = new KMeans(Config.iterations, K, docSet);
                    kMeansInstance.runAllIterations();
                }
            }
        }
    }
}
