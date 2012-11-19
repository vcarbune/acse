
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    private static Logger logger;
    private static String stopWordFile = null;
    private static String chartFile = "chart";

    private static Crawler crawler;
    private static DataSet dataSet;
    
    public static void initializeLogging() {
        try {
            InputStream inputStream = new FileInputStream("logging.properties");
            LogManager.getLogManager().readConfiguration(inputStream);

            FileHandler handler =
                    new FileHandler(Config.DYNAMIC_STATS_FILE, Config.LOG_FILE_SIZE, Config.LOG_FILE_COUNT);
            logger = Logger.getLogger(Main.class.getName());
            logger.addHandler(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initializeDataSet(String corpusFolder) {
        crawler = new Crawler(corpusFolder);
        
        try {
            if (Config.enableStopwordElimination == true) {
                if (stopWordFile == null) {
                    System.out.println("The stop word file was not given as parameter!"
                            + " When the stopWord flag is set also the file of stop words"
                            + " needs to be given as parameter!");
                    System.exit(1);
                }

                crawler.setStopWordsFile(stopWordFile);
                crawler.readStopWords();

                System.out.println("Stop Words Elimination Selected...");
            }

            dataSet = crawler.readDocSet();
       
        } catch (IOException e) {
            System.out.println("Could not read the documents. Exiting...");
            System.exit(1);
        }
    }
    
    public static void initializeFlags(String args[]) {

        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals(Config.PARAM_STOPWORD)) {
                Config.enableStopwordElimination = true;
                chartFile += "StopWord";
            } else if (args[i].equals(Config.PARAM_STEMMING)) {
                System.out.println("Stemmming is selected.....");
                Config.enableStemming = true;
                chartFile += "Stemming";
            } else if (args[i].startsWith(Config.PARAM_STOPWORDFILE)) {
                int eqPos = args[i].indexOf("=");
                stopWordFile = args[i].substring(eqPos + 1, args[i].length());
            } 
        }
    }

  

    public static void main(String args[]) throws IOException, FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: Main <document_folder>"
                    + " [" + Config.PARAM_STOPWORD + "]"
                    + " [" + Config.PARAM_STOPWORDFILE + "]"
                    + " [" + Config.PARAM_STEMMING + "]"
                    + " [" + Config.PARAM_FOLDER + "]"
            			);

            return;
        }

        initializeLogging();
        if (args.length >= 1) {
            initializeFlags(args);
        }
        initializeDataSet(args[0]);
    }

}
