import java.util.logging.Level;

public class Config {
    
    public static final Level LOG_LEVEL = Level.INFO;

    // Different files for static stats and dynamic stats
    public static final String STATIC_STATS_FILE = "static_stats.log";
    public static final String DYNAMIC_STATS_FILE = "dynamic_stats.log";

    // No log rotation is needed for now.
    public static final int LOG_FILE_COUNT = 5;
    public static final int LOG_FILE_SIZE = 102400;

    // Stopword file param
    public static final String PARAM_STOPWORDFILE = "--stopfile=";

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
}
