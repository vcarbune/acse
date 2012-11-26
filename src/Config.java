
import java.util.logging.Level;

public class Config {

    public static final Level LOG_LEVEL = Level.INFO;
    // Different files for static stats and dynamic stats
    public static final String STATIC_STATS_FILE = "static_stats.log";
    public static final String DYNAMIC_STATS_FILE = "dynamic_stats.log";
    // No log rotation is needed for now.
    public static final int LOG_FILE_COUNT = 5;
    public static final int LOG_FILE_SIZE = 1024000;
    // Stopword file param
    public static final String PARAM_STOPWORDFILE = "--stopfile=";
    // Nouns file param
    public static final String PARAM_NOUNSFILE = "--nounsfile=";
    // Query folder param
    public static final String PARAM_QUERYFOLDER = "--queryfolder=";
    public static final String PARAM_QUERYFILE = "--queryfile=";
    //RelevancyList file
    public static final String PARAM_RELEVANCY = "--relevancyList=";
    // Offline configuration flags
    public static final String PARAM_STOPWORD = "--stopword";
    public static boolean enableStopwordElimination = false;
    public static final String PARAM_STEMMING = "--stemming";
    public static boolean enableStemming = false;
    // Online switch between BASIC / 0, LOCAL / 1 and GLOBAL / 2 query expansion.
    public static int queryType = 0;
    
    // Alpha, Beta parameters for local expansion.
    public static double alpha = 0.5;
    public static double beta = 0.5;
    public static double gamma = 0.0;
    
    //Folder param for the Naive Bayesian
    public static final String PARAM_FOLDER = "--folder=";
    
    //Param to run the Kmeans
    public static final String PARAM_KMEANS = "--kmeans";
    public static boolean enableKmeans = false;
    
    public static int infK;
    public static int supK; 
    public static int iterations = 10; 
    public static int N = 10; 
    
}
