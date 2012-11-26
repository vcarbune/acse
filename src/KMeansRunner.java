import java.util.ArrayList;

public class KMeansRunner {
    ArrayList<DocEntry> docEntries;

    public KMeansRunner(ArrayList<DocEntry> docEntries) {
        this.docEntries = docEntries;
    }

    public void run() {
        for (int K = Config.infK; K < Config.supK; ++K) {
            for (int N = 0; N < Config.N; ++N) {
                KMeans kMeansInstance = new KMeans(Config.iterations, K, docEntries);
                kMeansInstance.runAllIterations();
            }
        }
    }
}
