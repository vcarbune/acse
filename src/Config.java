import java.util.logging.Level;

public class Config {
    public static final Level LOG_LEVEL = Level.INFO;
    
    // Different files for static stats and dynamic stats
    public static final String STATIC_STATS_FILE = "static_stats.log";
    public static final String DYNAMIC_STATS_FILE = "dynamic_stats.log";
    
    // No log rotation is needed for now.
    public static final int LOG_FILE_COUNT = 1;
    public static final int LOG_FILE_SIZE = 102400;
    
    //Stop Word file param
    public static final String PARAM_STOPWORDFILE = "--stopfile=";
    
    // Offline configuration flags
    public static final String PARAM_STOPWORD = "--stopword";
    public static boolean enableStopwordElimination = false;
    
    public static final String PARAM_STEMMING = "--stemming";

    public static boolean enableStemming = false;
  
    // Ordered proximity (due to ambiguity in the requirements)
    public static final String PARAM_ORDERED_PROXIMITY = "--ordered_proximity";
    public static boolean orderedProximity = false;
}
